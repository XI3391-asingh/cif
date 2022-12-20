package com.cif.cifservice.core.party.util;

import com.cif.cifservice.api.*;
import com.cif.cifservice.resources.exceptions.ServerSideException;
import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.db.elasticsearch.ElasticsearchAdminRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

public class CryptoUtil {

    public static final String FIELD_IS_NOT_PRESENT = "[Field = {}] is not present in object passed for encryption";
    private final Logger logger = getLogger(CryptoUtil.class);

    private static final String MODE_PADDING = "AES/ECB/PKCS5Padding"; // AES/ECB/PKCS5Padding it specify <algorithm>/<mode>/padding //decryption while using AES/ECB/PKCS5Padding
    private static final String ALGO = "AES";

    private final String secretKey;
    private  String encryptionFlag;

    private  List<String> fields;

    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private SecretKeySpec secretKeySpec;

    private final ElasticsearchAdminRepository adminRepository;

    private  AtomicBoolean configurationFlag = new AtomicBoolean(false);
    private  AtomicBoolean configurationSearchFlag = new AtomicBoolean(false);

    public CryptoUtil(EncryptionConfig encryptionConfig, ElasticsearchAdminRepository adminRepository) {
        this.secretKey = encryptionConfig.getSecretKey();
        this.adminRepository = adminRepository;
        try {
            this.secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGO);
            this.encryptCipher = Cipher.getInstance(MODE_PADDING);
            this.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            this.decryptCipher = Cipher.getInstance(MODE_PADDING);
            this.decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new ServerSideException("ENCRYPTION_CONFIG_ERROR", "Error occurred while setting up encrypt and decrypt cipher instance .Please contact the system administrator!", ex);
        }
    }

    public <T> T encryptObjectUsingConfiguredField(T object) {
        getConfigData();
        if (encryptionFlag.equalsIgnoreCase("FALSE")) {
            return (T) object;
        }
        final Set<String> tempFields = new CopyOnWriteArraySet<>();
        if (object instanceof Collection) {
            for (Object o : ((Collection<Object>) object)) {
                encryptObjectUsingConfiguredField(o);
            }
            return (T) object;
        }
        tempFields.addAll(this.fields);
        if (object instanceof PartyDistinctiveSearchCmd || object instanceof CreateAddressCmd
                || object instanceof UpdateAddressCmd || object instanceof CreateContactDetailsCmd
                || object instanceof UpdateContactDetailsCmd || object instanceof DedupeFieldRequestCmd) {
            encryptNonNestedObject(object, tempFields);
        } else {
            final String[] prevFieldName = {" "};
            tempFields.stream().filter(keyName -> !keyName.contains(prevFieldName[0])).forEach(keyName -> {
                try {
                    encryptNestedObject(object, prevFieldName, keyName);
                } catch (InvocationTargetException
                         | NoSuchMethodException
                         | IllegalAccessException
                         | NullPointerException e) {
                    logger.error(FIELD_IS_NOT_PRESENT, keyName);
                }

            });
        }
        return (T) object;
    }

    private <T> boolean encryptNestedObject(T object, String[] prevFieldName, String keyName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String fieldName = keyName.split("\\.")[0];
        if (BeanUtils.getProperty(object, fieldName) != null) {
            Object prop = PropertyUtils.getProperty(object, fieldName);
            if (prop instanceof Collection) {
                for (Object o : ((Collection<Object>) prop)) {
                    encryptObjectUsingConfiguredField(o);
                }
                prevFieldName[0] = fieldName;
                return true;
            }
            if (PropertyUtils.isReadable(object, fieldName)) {
                Optional<String> value = Optional.ofNullable(BeanUtils.getProperty(object, keyName));
                if (value.isPresent())
                    BeanUtils.setProperty(object, keyName, encrypt(value.get()));
            }
        }
        return false;
    }

    private <T> void encryptNonNestedObject(T object, Set<String> tempFields) {
        tempFields.forEach(field -> {
            String fieldName = field.split("\\.")[1];
            try {
                if (fieldName.equalsIgnoreCase("primaryMobileNUmber")) {
                    fieldName = "mobileNumber";
                }
                if (fieldName.equalsIgnoreCase("primaryEmail")) {
                    fieldName = "emailId";
                }
                if (PropertyUtils.isReadable(object, fieldName)) {
                    String value = BeanUtils.getProperty(object, fieldName);
                    BeanUtils.setProperty(object, fieldName, encrypt(value));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error(FIELD_IS_NOT_PRESENT, fieldName);
            }
        });
    }

    public <T> T decryptObjectUsingConfiguredField(T object) {
            getConfigData();
        if (encryptionFlag.equalsIgnoreCase("FALSE")) {
            return (T) object;
        }
        if (object instanceof Collection) {
            for (Object o : ((Collection<Object>) object)) {
                decryptObjectUsingConfiguredField(o);
            }
            return (T) object;
        }
        if (object instanceof PartyRecordCmd || object instanceof ViewAddressCmd
                || object instanceof ViewContactDetailsCmd) {
            decryptNonNestedObject(object);
        } else {
            final String[] prevFieldName = {" "};
            fields.stream().filter(keyName -> !keyName.contains(prevFieldName[0])).forEach(keyName -> {
                try {
                    decryptNestedObject(object, prevFieldName, keyName);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    logger.error(FIELD_IS_NOT_PRESENT, keyName);
                }
            });
        }
        return (T) object;
    }

    private <T> void decryptNestedObject(T object, String[] prevFieldName, String keyName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String fieldName = keyName.split("\\.")[0];
        if (BeanUtils.getProperty(object, fieldName) != null) {
            Object prop = PropertyUtils.getProperty(object, fieldName);
            if (prop instanceof Collection) {
                for (Object o : ((Collection<Object>) prop)) {
                    decryptObjectUsingConfiguredField(o);
                }
                prevFieldName[0] = fieldName;
                return;
            }
        }
        if (PropertyUtils.isReadable(object, fieldName)) {
            String value = BeanUtils.getProperty(object, keyName);
            BeanUtils.setProperty(object, keyName, decrypt(value));
        }
    }

    private <T> void decryptNonNestedObject(T object) {
        fields.forEach(field -> {
            String fieldName = field.split("\\.")[1];
            try {
                if (fieldName.equalsIgnoreCase("primaryMobileNUmber")) {
                    fieldName = "mobileNumber";
                }
                if (fieldName.equalsIgnoreCase("primaryEmail")) {
                    fieldName = "emailId";
                }
                if (PropertyUtils.isReadable(object, fieldName)) {
                    String value = BeanUtils.getProperty(object, fieldName);
                    BeanUtils.setProperty(object, fieldName, decrypt(value));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error(FIELD_IS_NOT_PRESENT, fieldName);
            }
        });
    }

    public String encrypt(String plainText) {
        if (isNotBlank(plainText) && encryptionFlag.equalsIgnoreCase("TRUE")) {
            try {
                byte[] cipherBytes = encryptCipher.doFinal(plainText.getBytes());
                return Base64.encodeBase64URLSafeString(cipherBytes);
            } catch (Exception e) {
                logger.error("Error occurred during encryption {} ", e.getMessage());
                return null;
            }
        }
        return plainText;
    }

    public String decrypt(String cipherText) {
        if (isNotBlank(cipherText) && encryptionFlag.equalsIgnoreCase("TRUE")) {
            try {
                return new String(decryptCipher.doFinal(Base64.decodeBase64(cipherText)));
            } catch (Exception e) {
                logger.error("Error occurred during decryption {} ", e.getMessage());
                return null;
            }
        }
        return cipherText;
    }

    public List<String> getFields() {
        return fields;
    }
    public void getConfigData() {
        if (configurationFlag.compareAndSet(false, true)) {
            Config configRecord = adminRepository.fetchAdminConfigRecordBasedOnType("ENCRYPTION");
            if (configRecord != null) {
                encryptionFlag = configRecord.getConfigData().stream().filter(configData -> "ENCRYPTIONFLAG".equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().get(0);
                fields = configRecord.getConfigData().stream().filter(configData -> "FIELDS".equalsIgnoreCase(configData.getKey())).findFirst().get().getValues();
            }
        }
    }

    public Config getUniversalSearchConfigData(Config prevConfig) {
        Config configRecord = prevConfig;
        if (configurationSearchFlag.compareAndSet(false, true)) {
            configRecord = adminRepository.fetchAdminConfigRecordBasedOnType("UNIVERSALSEARCHFIELDS");
            if (configRecord != null) {
                getConfigData();
            }
        }
        return configRecord;
    }

    public AtomicBoolean getConfigurationFlag() {
        return configurationFlag;
    }
    public AtomicBoolean getConfigurationSearchFlag() {
        return configurationSearchFlag;
    }
}
