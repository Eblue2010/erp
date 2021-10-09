package io.eliteblue.erp.core.util;

import io.eliteblue.erp.core.constants.*;
import io.eliteblue.erp.core.model.ContactInfo;
import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.model.ErpEmployeeID;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.repository.ErpEmployeeRepository;
import io.eliteblue.erp.core.service.ContactInfoService;
import io.eliteblue.erp.core.service.ErpEmployeeIDService;
import io.eliteblue.erp.core.service.ErpIDTypeService;
import io.eliteblue.erp.core.service.OperationsAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class EmpMastProcessUtil {

    private final DecimalFormat decimalFormat = new DecimalFormat("00000000");
    private final DecimalFormat areaFormat = new DecimalFormat("00");
    private final DateFormat dateFormat = new SimpleDateFormat("M/d/yy");
    private final DateFormat properDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Autowired
    private OperationsAreaService areaService;

    @Autowired
    private ErpEmployeeRepository repository;

    @Autowired
    private ErpEmployeeIDService idService;

    @Autowired
    private ErpIDTypeService idTypeService;

    @Autowired
    private ContactInfoService contactInfoService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    public void startProcess() throws Exception {
        Integer rows = ExcelUtils.getRowCount();
        String currentUserName = CurrentUserUtil.getFullName();
        // traverse all rows
        for(int x=1; x < rows; x++) {
            System.out.println("================================ ROW COUNT ["+x+"] ===============================");
            ErpEmployee employee = new ErpEmployee();
            String deployedRaw = (String) ExcelUtils.getCellData(x, 212);
            String deployed = (deployedRaw.contains("/")) ? deployedRaw.split("/")[0] : deployedRaw;
            OperationsArea area = areaService.findByLocation(deployed);
            String operationAreaCode = "99";
            if(area != null) {
                //System.out.println("AREA: " + area.getLocation());
                operationAreaCode = areaFormat.format(area.getId());
            }
            else {
                area = areaService.findByLocation("HEAD OFFICE");
                operationAreaCode = areaFormat.format(area.getId());
            }
            //System.out.println("SEQUENCE [emp_area_"+operationAreaCode+"_seq]");
            BigDecimal seq = repository.getNextSequenceByName("emp_area_"+operationAreaCode+"_seq");
            Long seqNum = seq.longValue();
            /*if(seq.intValue() <= 1) {
                Integer isEmpty = repository.getIsEmpty();
                if(isEmpty != 0){
                    seqNum += 1;
                }
            }
            else {
                seqNum += 1;
            }*/

            String employeeId = operationAreaCode + decimalFormat.format(seqNum);
            //System.out.println("EMPLOYEE ID: "+employeeId);

            employee.setEmployeeId(employeeId);
            employee.setOperation(DataOperation.CREATED.name());
            employee.setLastEditedBy(currentUserName);
            employee.setCreatedBy(currentUserName);
            employee.setLastUpdate(new Date());
            employee.setDateCreated(new Date());

            String firstName = (String) ExcelUtils.getCellData(x, 2);
            String lastName = (String) ExcelUtils.getCellData(x, 1);
            System.out.println(firstName+" "+lastName);

            if(firstName.isEmpty() || lastName.isEmpty()) {
                continue;
            }

            employee.setFirstname(firstName);
            employee.setLastname(lastName);
            employee.setMiddlename((String) ExcelUtils.getCellData(x, 3));
            employee.setExtname((String) ExcelUtils.getCellData(x, 4));
            employee.setBirthPlace((String) ExcelUtils.getCellData(x, 6));
            String birthdate = (String) ExcelUtils.getCellData(x, 5);
            System.out.println("Birthdate: "+birthdate);
            if(birthdate != null && !birthdate.isEmpty()) {
                employee.setBirthDate(parseStringDate(birthdate));
            }
            String gender = (String) ExcelUtils.getCellData(x, 10);
            gender = gender.replaceAll(".", "");
            if(gender != null && !gender.isEmpty()) {
                employee.setGender(Gender.valueOf(gender));
            }
            if(employee.getGender() == null) {
                employee.setGender(Gender.MALE);
            }
            String civilStatus = (String) ExcelUtils.getCellData(x, 11);
            if(civilStatus != null && !civilStatus.isEmpty()) {
                employee.setCivilStatus(parseMaritalStatus(civilStatus.toUpperCase()));
            }
            employee.setStatus(EmployeeStatus.HIRED);
            String joined = (String) ExcelUtils.getCellData(x, 21);
            if(joined != null && !joined.isEmpty()) {
                employee.setJoinedDate(parseStringDate(joined));
            }
            employee.setMarks((String) ExcelUtils.getCellData(x, 15));
            String blood = (String) ExcelUtils.getCellData(x, 12);
            if(blood != null && !blood.isEmpty()) {
                employee.setBloodType(getBloodTypeFromString(blood));
            }

            employee = processWeight(employee, (String) ExcelUtils.getCellData(x, 14));
            employee.setHeightCentimeters(parseHeight((String) ExcelUtils.getCellData(x, 13)));
            employee.setPosition((String) ExcelUtils.getCellData(x, 23));
            employee.setBankName((String) ExcelUtils.getCellData(x, 209));
            employee.setBankAccountNumber((String) ExcelUtils.getCellData(x, 20));
            Set<ErpEmployeeID> ids = new HashSet<>();
            ErpEmployeeID temp = new ErpEmployeeID();
            String sssNo = (String) ExcelUtils.getCellData(x, 16);
            if(sssNo != null && !sssNo.isEmpty()) {
                temp.setIdName(idTypeService.findById(3L));
                temp.setEmployee(employee);
                temp.setIdentificationNumber(sssNo);
                temp.setCreatedBy(currentUserName);
                temp.setLastEditedBy(currentUserName);
                temp.setDateCreated(new Date());
                temp.setLastUpdate(new Date());
                temp.setOperation(DataOperation.CREATED.name());
                ids.add(temp);
            }
            String tinNo = (String) ExcelUtils.getCellData(x, 17);
            if(tinNo != null && !tinNo.isEmpty()) {
                temp = new ErpEmployeeID();
                temp.setIdName(idTypeService.findById(4L));
                temp.setEmployee(employee);
                temp.setIdentificationNumber(tinNo);
                temp.setCreatedBy(currentUserName);
                temp.setLastEditedBy(currentUserName);
                temp.setDateCreated(new Date());
                temp.setLastUpdate(new Date());
                temp.setOperation(DataOperation.CREATED.name());
                ids.add(temp);
            }
            String philHealth = (String) ExcelUtils.getCellData(x, 18);
            if(philHealth != null && !philHealth.isEmpty()) {
                temp = new ErpEmployeeID();
                temp.setIdName(idTypeService.findById(1L));
                temp.setEmployee(employee);
                temp.setIdentificationNumber(philHealth);
                temp.setCreatedBy(currentUserName);
                temp.setLastEditedBy(currentUserName);
                temp.setDateCreated(new Date());
                temp.setLastUpdate(new Date());
                temp.setOperation(DataOperation.CREATED.name());
                ids.add(temp);
            }
            String pagibig = (String) ExcelUtils.getCellData(x, 19);
            if(pagibig != null && !pagibig.isEmpty()) {
                temp = new ErpEmployeeID();
                temp.setIdName(idTypeService.findById(2L));
                temp.setEmployee(employee);
                temp.setIdentificationNumber(pagibig);
                temp.setCreatedBy(currentUserName);
                temp.setLastEditedBy(currentUserName);
                temp.setDateCreated(new Date());
                temp.setLastUpdate(new Date());
                temp.setOperation(DataOperation.CREATED.name());
                ids.add(temp);
            }
            String licenseNo = (String) ExcelUtils.getCellData(x, 22);
            if(licenseNo != null && !licenseNo.isEmpty()) {
                temp = new ErpEmployeeID();
                temp.setIdName(idTypeService.findById(5L));
                temp.setEmployee(employee);
                temp.setIdentificationNumber(licenseNo);
                temp.setCreatedBy(currentUserName);
                temp.setLastEditedBy(currentUserName);
                temp.setDateCreated(new Date());
                temp.setLastUpdate(new Date());
                temp.setOperation(DataOperation.CREATED.name());
                ids.add(temp);
            }
            employee.setErpEmployeeIDList(ids);
            ContactInfo info = processContactInfo(employee, (String) ExcelUtils.getCellData(x, 7), currentUserName);
            Set<ContactInfo> contacts = new HashSet<>();
            if(info != null) {
                contacts.add(info);
                employee.setContacts(contacts);
            }
            repository.save(employee);
            for(ErpEmployeeID i: ids) {
                idService.save(i);
            }
            for(ContactInfo c: contacts) {
                contactInfoService.save(c);
            }
        }
    }

    private ContactInfo processContactInfo(ErpEmployee employee, String input, String currentUserName) {
        String cleanedInput = input;
        if(input == null || input.isEmpty() || input.toUpperCase().equals("0") || input.length() < 2) {
            return null;
        }
        if(cleanedInput.length() == 10) {
            cleanedInput = "0"+input;
        }
        ContactInfo info = new ContactInfo();
        info.setContactNumber(cleanedInput);
        info.setEmployee(employee);
        info.setCreatedBy(currentUserName);
        info.setLastEditedBy(currentUserName);
        info.setDateCreated(new Date());
        info.setLastUpdate(new Date());
        info.setOperation(DataOperation.CREATED.name());
        return info;
    }

    private ErpEmployee processWeight(ErpEmployee employee, String input) {
        if(input == null || input.isEmpty() || input.toUpperCase().equals("N/A")) {
            return employee;
        }

        String weight = input.toUpperCase();
        weight = weight.replaceAll("[^0-9]", "");
        if(!weight.isEmpty()) {
            if (weight.length() <= 2) {
                employee.setWeightKilo(Double.parseDouble(weight));
                employee.setWeightPound(employee.getWeightKilo() / 0.454);
            } else {
                employee.setWeightPound(Double.parseDouble(weight));
                employee.setWeightKilo(employee.getWeightPound() * 0.454);
            }
        }
        return employee;
    }

    private Double parseHeight(String input) {
        if(input == null || input.isEmpty() || input.toUpperCase().equals("N/A")) {
            return null;
        }
        Double retVal = 0.0;
        String height = input;
        if(input.contains("'") || input.contains("\"") || input.contains("/")) {
            String feet = "";
            String inches = "";
            if(input.contains(" ")){
                height = height.substring(height.indexOf(" ")+1);
                height.trim();
            }
            if(height.contains("'")) {
                if((height.indexOf("'")+1) == height.length()) {
                    feet = ""+height.charAt(0);
                    inches = (""+height.charAt(1)).replaceAll("[^0-9]", "");
                } else {
                    feet = height.split("'")[0];
                    inches = height.split("'")[1];
                    inches = inches.replaceAll("[^0-9]", "");
                }
            } else if(height.contains("\"")) {
                feet = height.split("\"")[0];
                inches = height.split("\"")[1];
                inches = inches.replaceAll("[^0-9]", "");
            } else if(height.contains("/")) {
                feet = height.split("/")[0];
                inches = height.split("/")[1];
                inches = inches.replaceAll("[^0-9]", "");
            }
            if (feet != null && !feet.isEmpty()) {
                retVal = 30.48 * Double.parseDouble(feet);
            }
            if (inches != null && !inches.isEmpty()) {
                retVal += 2.54 * Double.parseDouble(inches);
            }
        } else if(input.length() == 1) {
            retVal = 30.48 * Double.parseDouble(input);
        } else {
            height = height.replaceAll("[^0-9]", "");
            retVal = Double.parseDouble(height);
        }
        return retVal;
    }

    private Date parseStringDate(String input) {
        try {
            Date retVal = dateFormat.parse(input);
            return retVal;
        } catch (Exception e) {
            return null;
        }
    }

    private CivilStatus parseMaritalStatus(String input) {
        switch (input) {
            case "LIVE IN":
                return CivilStatus.LIVE_IN;
            case "WIDOW":
                return CivilStatus.WIDOWED;
            case "WIDDOW":
                return CivilStatus.WIDOWED;
            case "WIDOWER":
                return CivilStatus.WIDOWED;
            case "SEPERATED":
                return CivilStatus.SEPARATED;
            case "MARRIAGE":
                return CivilStatus.MARRIED;
            default:
                return CivilStatus.valueOf(input);
        }
    }

    private BloodType getBloodTypeFromString(String input) {
        switch (input) {
            case "A+":
                return BloodType.A_PLUS;
            case "A-":
                return BloodType.A_MINUS;
            case "B+":
                return BloodType.B_PLUS;
            case "B-":
                return BloodType.B_MINUS;
            case "O+":
                return BloodType.O_PLUS;
            case "O-":
                return BloodType.O_MINUS;
            case "AB-":
                return BloodType.AB_MINUS;
            case "AB+":
                return BloodType.AB_PLUS;
            default:
                return null;
        }
    }
}
