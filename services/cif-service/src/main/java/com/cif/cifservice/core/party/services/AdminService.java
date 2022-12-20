package com.cif.cifservice.core.party.services;

import com.cif.cifservice.core.party.domain.*;
import com.cif.cifservice.core.party.util.BulkImportConfig;
import com.cif.cifservice.db.PartyRepository;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.db.elasticsearch.ElasticsearchAdminRepository;
import com.cif.cifservice.resources.exceptions.DatabasePersistException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.cif.cifservice.core.party.helper.ResponseHelper.*;
import static org.slf4j.LoggerFactory.getLogger;


public class AdminService {
    private final Logger logger = getLogger(AdminService.class);

    private final ElasticsearchAdminRepository adminRepository;
    private final CryptoUtil cryptoUtil;
    private final PartyRepository partyRepository;

    public static final String BULK_IMPORT_ERROR = "Bulk_IMPORT_ERROR";

    private final String tempPath;

    public AdminService(ElasticsearchAdminRepository adminRepository, CryptoUtil cryptoUtil, PartyRepository partyRepository, BulkImportConfig bulkImportConfig) {
        this.adminRepository = adminRepository;
        this.cryptoUtil = cryptoUtil;
        this.partyRepository = partyRepository;
        this.tempPath = bulkImportConfig.getTempPath();
    }

    public Response save(Config configData) {
        int statusCode = adminRepository.addConfigRecord(configData);
        if (statusCode == 201 || statusCode == 200) {
            if ("ENCRYPTION".equalsIgnoreCase(configData.getType())) {
                cryptoUtil.getConfigurationFlag().set(false);
            } else if ("UNIVERSALSEARCHFIELDS".equalsIgnoreCase(configData.getType())) {
                cryptoUtil.getConfigurationSearchFlag().set(false);
            }
            return Response.status(statusCode).entity(buildSuccessResponse(statusCode == 201 ? "CONFIG_CREATED" : "CONFIG_UPDATED",
                    "Config record persist success", configData)).build();
        } else {
            return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error occurred while saving config record!")).build();
        }
    }

    public Response fetchConfigByRecord(String type) {
        Config configRecord = adminRepository.fetchAdminConfigRecordBasedOnType(type);
        if (configRecord != null) {
            return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONFIG_FETCH", "Config record found!", configRecord)).build();
        } else {
            logger.info("No config record(s) found for type {} ", type);
            return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found !")).build();
        }
    }

    public Response saveBulkImportData(InputStream fileData) {
        logger.info("Step2 : Inside saveBulkImportData method -- file data {}", fileData);
        File file = new File(tempPath);
        logger.info("creating file for path {}", tempPath);
        FileInputStream fileInputStream = null;
        XSSFWorkbook workbook = null;
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.copy(fileData, Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
            logger.info("copying file data {}", file.getPath());

            fileInputStream = new FileInputStream(file.getPath());
            logger.info("fileInputStream {}", fileInputStream);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            logger.info("setting zip min inflate ratio");
            workbook = new XSSFWorkbook(fileInputStream);
            logger.info("creating workbook {}", workbook);
            List<AddressMaster> addressMasterList = new ArrayList<>();
            List<CountryMaster> countryMasterList = new ArrayList<>();
            List<LookupMaster> lookupMasterList = new ArrayList<>();
            DataFormatter df = new DataFormatter();
            BulkImport bulkImport = new BulkImport();
            workbook.forEach(sheet -> {
                switch (sheet.getSheetName()) {
                    case "ADDRESS_MASTER":
                        extractAddressMasterSheetData(sheet, df, addressMasterList);
                        logger.info("Step4 : Outside extractAddressMasterSheetData method");
                        bulkImport.setTotalRecordsAddressMaster(addressMasterList.size());
                        break;
                    case "COUNTRY_MASTER":
                        extractCountryMasterSheetData(sheet, df, countryMasterList);
                        logger.info("Step6 : Outside extractCountryMasterSheetData method");
                        bulkImport.setTotalRecordsCountryMaster(countryMasterList.size());
                        break;
                    case "LOOKUP_MASTER":
                        extractLookupMasterSheetData(sheet, df, lookupMasterList);
                        logger.info("Step8 : Outside extractCountryMasterSheetData method");

                        bulkImport.setTotalRecordsLookupMaster(lookupMasterList.size());
                        break;
                    default:
                }
            });
            logger.info("Step9 : Filtering invalid parent type ");

            List<AddressMaster> addressMasterErrorList = addressMasterList.stream()
                    .filter(addressMaster -> (StringUtils.isNotBlank(addressMaster.getErrorType()) && addressMaster.getErrorType().contains("Invalid parent type"))).collect(Collectors.toList());
            addressMasterList.removeAll(addressMasterErrorList);
            logger.info("Step10 :  invalid parent type removed");

            List<AddressMaster> addressDuplicateRecords = new ArrayList<>();
            int addressMasterSuccessRecords = saveAddressMaster(addressMasterList, addressDuplicateRecords);
            logger.info("Step12 :  Outside saveAddressMaster method");
            addressMasterErrorList.addAll(addressDuplicateRecords);

            List<CountryMaster> countryDuplicateRecords = new ArrayList<>();
            int countryMasterSuccessRecords = saveCountryMaster(countryMasterList, countryDuplicateRecords);
            logger.info("Step14 :  Outside saveCountryMaster method");

            List<LookupMaster> lookupDuplicateRecords = new ArrayList<>();
            int lookupMasterSuccessRecords = saveLookupMaster(lookupMasterList, lookupDuplicateRecords);
            logger.info("Step16 :  Outside saveLookupMaster method");

            Workbook feedbackWorkbook = createErrorExcel(addressMasterErrorList, countryDuplicateRecords, lookupDuplicateRecords);
            logger.info("Step24 :  Outside createErrorExcel method");
            StreamingOutput dataStream = output -> {
                logger.info("Step25 :  Writing feedbackWorkbook in output stream");
                feedbackWorkbook.write(output);
                feedbackWorkbook.close();
            };
            logger.info("Step26 :  Writing output in byte stream");
            dataStream.write(byteStream);
            logger.info("Step27 :  setting data in bulk import dto");
            bulkImport.setSuccessRecordsAddressMaster(addressMasterSuccessRecords);
            bulkImport.setFailedRecordsAddressMaster(bulkImport.getTotalRecordsAddressMaster() - addressMasterSuccessRecords);
            bulkImport.setSuccessRecordsCountryMaster(countryMasterSuccessRecords);
            bulkImport.setFailedRecordsCountryMaster(bulkImport.getTotalRecordsCountryMaster() - countryMasterSuccessRecords);
            bulkImport.setSuccessRecordsLookupMaster(lookupMasterSuccessRecords);
            bulkImport.setFailedRecordsLookupMaster(bulkImport.getTotalRecordsLookupMaster() - lookupMasterSuccessRecords);
            logger.info("Step28 :  encoding in Base64 and setting the output in bulk import dto");
            bulkImport.setOutput(new String(Base64.getEncoder().encode(byteStream.toByteArray())));
            logger.info("Step29 :  setting the data in response and return back the response");
            fileInputStream.close();
            workbook.close();
            return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("BULK_IMPORT", "Bulk import success!", bulkImport)).build();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new DatabasePersistException(BULK_IMPORT_ERROR, "Error occurred while bulk importing the records. Please contact the system administrator ! ", exception);
        } finally {
            try {
                fileInputStream.close();
                workbook.close();
                file.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void extractAddressMasterSheetData(Sheet sheet, DataFormatter df, List<AddressMaster> addressMasterList) {
        logger.info("Step3 : Inside extractAddressMasterSheetData method");
        Map<String, Integer> map = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        for (int colIx = headerRow.getFirstCellNum(); colIx < headerRow.getLastCellNum(); colIx++) { //loop from first to last index
            map.put(headerRow.getCell(colIx).getStringCellValue().toLowerCase(), headerRow.getCell(colIx).getColumnIndex());
        }
        sheet.forEach(row -> {
            if (row.getRowNum() > 0 && !checkIfRowIsEmpty(row)) {
                AddressMaster addressMaster = new AddressMaster();
                row.forEach(cell -> {
                    int cellIndex = cell.getColumnIndex();
                    if (cellIndex == map.get("Type".toLowerCase())) {
                        addressMaster.setType(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("Code".toLowerCase())) {
                        addressMaster.setCode(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("Description".toLowerCase())) {
                        addressMaster.setDescription(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("Parent Code".toLowerCase())) {
                        addressMaster.setParentCode(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("Parent Type".toLowerCase())) {
                        String parentType = df.formatCellValue(cell);
                        addressMaster.setParentType(parentType);
                        if (StringUtils.isNotBlank(addressMaster.getType())) {
                            switch (addressMaster.getType().toUpperCase()) {
                                case "WARD":
                                    if (!ParentDetail.WARD.getValue().equalsIgnoreCase(addressMaster.getParentType())) {
                                        addressMaster.setErrorType("Invalid parent type: parent type for Ward is City");
                                    }
                                    break;
                                case "CITY":
                                    if (!ParentDetail.CITY.getValue().equalsIgnoreCase(addressMaster.getParentType())) {
                                        addressMaster.setErrorType("Invalid parent type: parent type for City is District");
                                    }
                                    break;
                                case "DISTRICT":
                                    if (!ParentDetail.DISTRICT.getValue().equalsIgnoreCase(addressMaster.getParentType())) {
                                        addressMaster.setErrorType("Invalid parent type: parent type for District is Country");
                                    }
                                    break;
                                default:
                                    addressMaster.setErrorType("No parent type found");
                            }
                        }
                    }
                });
                addressMasterList.add(addressMaster);
            }
        });
    }

    private void extractCountryMasterSheetData(Sheet sheet, DataFormatter df, List<CountryMaster> countryMasterList) {
        logger.info("Step5 : Inside extractCountryMasterSheetData method");
        Map<String, Integer> map = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        for (int colIx = headerRow.getFirstCellNum(); colIx < headerRow.getLastCellNum(); colIx++) { //loop from first to last index
            map.put(headerRow.getCell(colIx).getStringCellValue().toLowerCase(), headerRow.getCell(colIx).getColumnIndex());
        }
        sheet.forEach(row -> {
            CountryMaster countryMaster = new CountryMaster();
            if (row.getRowNum() > 0 && !checkIfRowIsEmpty(row)) {
                row.forEach(cell -> {
                    int cellIndex = cell.getColumnIndex();
                    if (cellIndex == map.get("Code".toLowerCase())) {
                        countryMaster.setCode(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("Description".toLowerCase())) {
                        countryMaster.setDescription(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("ISD Code".toLowerCase())) {
                        countryMaster.setIsdCode(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("ISO Code".toLowerCase())) {
                        countryMaster.setIsoCode(df.formatCellValue(cell));
                    }
                });
                countryMasterList.add(countryMaster);
            }
        });
    }

    private void extractLookupMasterSheetData(Sheet sheet, DataFormatter df, List<LookupMaster> lookupMasterList) {
        logger.info("Step7 : Inside extractLookupMasterSheetData method");

        Map<String, Integer> map = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        for (int colIx = headerRow.getFirstCellNum(); colIx < headerRow.getLastCellNum(); colIx++) { //loop from first to last index
            map.put(headerRow.getCell(colIx).getStringCellValue().toLowerCase(), headerRow.getCell(colIx).getColumnIndex());
        }
        sheet.forEach(row -> {
            if (row.getRowNum() > 0 && !checkIfRowIsEmpty(row)) {
                LookupMaster lookupMaster = new LookupMaster();
                row.forEach(cell -> {
                    int cellIndex = cell.getColumnIndex();
                    if (cellIndex == map.get("Type".toLowerCase())) {
                        lookupMaster.setType(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("Code".toLowerCase())) {
                        lookupMaster.setCode(df.formatCellValue(cell));
                    } else if (cellIndex == map.get("Description".toLowerCase())) {
                        lookupMaster.setDescription(df.formatCellValue(cell));
                    }
                });
                lookupMasterList.add(lookupMaster);
            }
        });
    }

    public Workbook createErrorExcel(List<AddressMaster> addressMasterErrorList, List<CountryMaster> countryMasterErrorList, List<LookupMaster> lookupMasterErrorList) throws IOException {
        logger.info("Step17 :  Inside createErrorExcel method");

        Workbook workbook = new HSSFWorkbook();
        Sheet addressMasterSheet = workbook.createSheet("ADDRESS_MASTER");
        Sheet countryMasterSheet = workbook.createSheet("COUNTRY_MASTER");
        Sheet lookupMasterSheet = workbook.createSheet("LOOKUP_MASTER");
        workbook.forEach(sheet -> {
            switch (sheet.getSheetName()) {
                case "ADDRESS_MASTER":
                    writeErrorExcelDataForAddressMaster(addressMasterSheet, addressMasterErrorList);
                    logger.info("Step19 :  Outside writeErrorExcelDataForAddressMaster method");

                    break;
                case "COUNTRY_MASTER":
                    writeErrorExcelDataForCountryMaster(countryMasterSheet, countryMasterErrorList);
                    logger.info("Step21 :  Outside writeErrorExcelDataForCountryMaster method");

                    break;
                case "LOOKUP_MASTER":
                    writeErrorExcelDataForLookupMaster(lookupMasterSheet, lookupMasterErrorList);
                    logger.info("Step23 :  Outside writeErrorExcelDataForLookupMaster method");

                    break;
                default:
            }
        });
        return workbook;
    }


    public int saveAddressMaster(List<AddressMaster> addressMasterList, List<AddressMaster> addressDuplicateRecords) {
        logger.info("Step11 :  Inside saveAddressMaster method");

        List<AddressMaster> addressMasterDBList = partyRepository.findAllAddressMasterDetails();
        List<AddressMaster> excelDBList = new ArrayList<>();
        excelDBList.addAll(addressMasterDBList);
        excelDBList.addAll(addressMasterList);
        addressDuplicateRecords.addAll(excelDBList.stream()
                .collect(Collectors.groupingBy(p -> p.getType() + "-" + p.getCode(), Collectors.toList()))
                .values()
                .stream()
                .filter(i -> i.size() > 1)
                .flatMap(j -> j.stream()).distinct()
                .collect(Collectors.toList()));

        addressDuplicateRecords.stream().forEach(addressMaster -> addressMaster.setErrorType("Duplicate record"));
        addressMasterList.removeAll(addressDuplicateRecords);
        return partyRepository.saveAddressMaster(addressMasterList).length;
    }

    public int saveCountryMaster(List<CountryMaster> countryMasterList, List<CountryMaster> countryDuplicateRecords) {
        logger.info("Step13 :  inside saveCountryMaster method");

        List<CountryMaster> countryMasterDBList = partyRepository.findAllCountryMasterDetails();
        List<CountryMaster> excelDBList = new ArrayList<>();
        excelDBList.addAll(countryMasterDBList);
        excelDBList.addAll(countryMasterList);
        countryDuplicateRecords.addAll(excelDBList.stream()
                .collect(Collectors.groupingBy(p -> p.getCode() + "-" + p.getDescription(), Collectors.toList()))
                .values()
                .stream()
                .filter(i -> i.size() > 1)
                .flatMap(j -> j.stream()).distinct()
                .collect(Collectors.toList()));

        countryDuplicateRecords.stream().forEach(countryMaster -> countryMaster.setErrorType("Duplicate record"));
        countryMasterList.removeAll(countryDuplicateRecords);
        return partyRepository.saveCountryMaster(countryMasterList).length;
    }

    public int saveLookupMaster(List<LookupMaster> lookupMasterList, List<LookupMaster> lookupDuplicateRecords) {
        logger.info("Step15 :  inside saveLookupMaster method");

        List<LookupMaster> lookupMasterDBList = partyRepository.findAllLookupMasterDetails();
        List<LookupMaster> excelDBList = new ArrayList<>();
        excelDBList.addAll(lookupMasterDBList);
        excelDBList.addAll(lookupMasterList);
        lookupDuplicateRecords.addAll(excelDBList.stream()
                .collect(Collectors.groupingBy(p -> p.getType() + "-" + p.getCode(), Collectors.toList()))
                .values()
                .stream()
                .filter(i -> i.size() > 1)
                .flatMap(j -> j.stream()).distinct()
                .collect(Collectors.toList()));

        lookupDuplicateRecords.stream().forEach(lookupMaster -> lookupMaster.setErrorType("Duplicate record"));
        lookupMasterList.removeAll(lookupDuplicateRecords);
        return partyRepository.saveLookupMaster(lookupMasterList).length;
    }

    public void writeErrorExcelDataForAddressMaster(Sheet sheet, List<AddressMaster> addressMasterErrorList) {
        logger.info("Step18 :  Inside writeErrorExcelDataForAddressMaster method");

        Row headerRow = sheet.createRow(0);
        Cell typeCell = headerRow.createCell(0);
        typeCell.setCellValue("TYPE");
        Cell codeCell = headerRow.createCell(1);
        codeCell.setCellValue("CODE");
        Cell descriptionCell = headerRow.createCell(2);
        descriptionCell.setCellValue("DESCRIPTION");
        Cell parentTypeCell = headerRow.createCell(3);
        parentTypeCell.setCellValue("PARENT TYPE");
        Cell parentCodeCell = headerRow.createCell(4);
        parentCodeCell.setCellValue("PARENT CODE");
        Cell errorCell = headerRow.createCell(5);
        errorCell.setCellValue("ERROR");

        AtomicInteger rowNum = new AtomicInteger(0);
        addressMasterErrorList.forEach(addressMaster -> {
            Row row = sheet.createRow(rowNum.incrementAndGet());
            row.createCell(0).setCellValue(addressMaster.getType());
            row.createCell(1).setCellValue(addressMaster.getCode());
            row.createCell(2).setCellValue(addressMaster.getDescription());
            row.createCell(3).setCellValue(addressMaster.getParentType());
            row.createCell(4).setCellValue(addressMaster.getParentCode());
            row.createCell(5).setCellValue(addressMaster.getErrorType());
        });
    }

    public void writeErrorExcelDataForCountryMaster(Sheet sheet, List<CountryMaster> countryMasterErrorList) {
        logger.info("Step20 :  Inside writeErrorExcelDataForCountryMaster method");

        Row headerRow = sheet.createRow(0);
        Cell typeCell = headerRow.createCell(0);
        typeCell.setCellValue("CODE");
        Cell codeCell = headerRow.createCell(1);
        codeCell.setCellValue("DESCRIPTION");
        Cell descriptionCell = headerRow.createCell(2);
        descriptionCell.setCellValue("ISD CODE");
        Cell parentTypeCell = headerRow.createCell(3);
        parentTypeCell.setCellValue("ISO CODE");
        Cell parentCodeCell = headerRow.createCell(4);
        parentCodeCell.setCellValue("ERROR");

        AtomicInteger rowNum = new AtomicInteger(0);
        countryMasterErrorList.forEach(countryMaster -> {
            Row row = sheet.createRow(rowNum.incrementAndGet());
            row.createCell(0).setCellValue(countryMaster.getCode());
            row.createCell(1).setCellValue(countryMaster.getDescription());
            row.createCell(2).setCellValue(countryMaster.getIsdCode());
            row.createCell(3).setCellValue(countryMaster.getIsoCode());
            row.createCell(4).setCellValue(countryMaster.getErrorType());
        });
    }

    public void writeErrorExcelDataForLookupMaster(Sheet sheet, List<LookupMaster> lookupMasterErrorList) {
        logger.info("Step22 :  Inside writeErrorExcelDataForLookupMaster method");

        Row headerRow = sheet.createRow(0);
        Cell typeCell = headerRow.createCell(0);
        typeCell.setCellValue("TYPE");
        Cell codeCell = headerRow.createCell(1);
        codeCell.setCellValue("CODE");
        Cell descriptionCell = headerRow.createCell(2);
        descriptionCell.setCellValue("DESCRIPTION");
        Cell parentTypeCell = headerRow.createCell(3);
        parentTypeCell.setCellValue("ERROR");

        AtomicInteger rowNum = new AtomicInteger(0);
        lookupMasterErrorList.forEach(lookupMaster -> {
            Row row = sheet.createRow(rowNum.incrementAndGet());
            row.createCell(0).setCellValue(lookupMaster.getType());
            row.createCell(1).setCellValue(lookupMaster.getCode());
            row.createCell(2).setCellValue(lookupMaster.getDescription());
            row.createCell(3).setCellValue(lookupMaster.getErrorType());
        });
    }
    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }
    enum ParentDetail {
        WARD("City"), CITY("District"), DISTRICT("Country");
        private final String value;

        ParentDetail(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
