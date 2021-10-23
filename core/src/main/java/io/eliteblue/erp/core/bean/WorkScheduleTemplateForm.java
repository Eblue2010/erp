package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.constants.ApprovalStatus;
import io.eliteblue.erp.core.constants.WorkSchedLegend;
import io.eliteblue.erp.core.model.*;
import io.eliteblue.erp.core.service.ErpDetachmentService;
import io.eliteblue.erp.core.service.WorkAssignmentService;
import io.eliteblue.erp.core.service.WorkDayService;
import io.eliteblue.erp.core.service.WorkScheduleService;
import io.eliteblue.erp.core.util.ExcelUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class WorkScheduleTemplateForm implements Serializable {

    private final DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    @Autowired
    private WorkScheduleService workScheduleService;

    @Autowired
    private WorkAssignmentService workAssignmentService;

    @Autowired
    private WorkDayService workDayService;

    private List<ErpDetachment> detachments;

    private UploadedFile file;

    @PostConstruct
    public void init() {
        detachments = erpDetachmentService.getAllFilteredLocation();
    }

    public List<ErpDetachment> getDetachments() {
        return detachments;
    }

    public void setDetachments(List<ErpDetachment> detachments) {
        this.detachments = detachments;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String backBtnPressed() { return "workschedules?faces-redirect=true&includeViewParams=true"; }

    public void fileUpload() throws Exception {
        System.out.println("FILE: "+file);
        if (file != null) {
            ExcelUtils.initializeWithInputStream(file.getInputStream(), "Sheet1");
            String detachmentStringId = (String) ExcelUtils.getCellData(0,1);
            if(!has(detachmentStringId) || detachmentStringId.isEmpty()) {
                addDetailMessage("FAILED UPLOAD", file.getFileName() + " has no Detachment ID.", FacesMessage.SEVERITY_ERROR);
            } else {
                Long detachmentId = Long.parseLong(detachmentStringId);
                System.out.println("Detachment ID: " + detachmentId);
                ErpDetachment detachment = erpDetachmentService.findById(detachmentId);
                ErpWorkSchedule erpWorkSchedule = new ErpWorkSchedule();
                erpWorkSchedule.setErpDetachment(detachment);
                erpWorkSchedule.setStatus(ApprovalStatus.PENDING);
                String startDateString = (String) ExcelUtils.getCellData(1, 1);
                String stopDateString = (String) ExcelUtils.getCellData(2, 1);
                Date startDate = new Date();
                Date stopDate = new Date();
                if(has(startDateString) && !startDateString.isEmpty() && has(stopDateString) && !stopDateString.isEmpty()) {
                    startDate = format.parse(startDateString);
                    stopDate = format.parse(stopDateString);
                    LocalDate ld_start = convertToLocalDateViaInstant(startDate);
                    LocalDate ld_stop = convertToLocalDateViaInstant(stopDate);
                    erpWorkSchedule.setStartDate(startDate);
                    erpWorkSchedule.setStopDate(stopDate);
                    System.out.println("START: "+startDate);
                    System.out.println("STOP: "+stopDate);
                    String shiftInCharge = (String) ExcelUtils.getCellData(2,3);
                    String detachmentCommander = (String) ExcelUtils.getCellData(2,8);
                    String areaSecurityCommander = (String) ExcelUtils.getCellData(2,13);
                    String description = "Work Schedule "+ld_start.format(DateTimeFormatter.ofPattern("MMM dd-"))+ld_stop.format(DateTimeFormatter.ofPattern("dd yyyy"));
                    System.out.println("in charge: "+shiftInCharge);
                    System.out.println("DC: "+detachmentCommander);
                    System.out.println("ASC: "+areaSecurityCommander);
                    System.out.println("description: "+description);
                    erpWorkSchedule.setShiftCommander(shiftInCharge);
                    erpWorkSchedule.setDetachmentCommander(detachmentCommander);
                    erpWorkSchedule.setAreaSecurityCommander(areaSecurityCommander);
                    erpWorkSchedule.setDescription(description);

                    if(has(detachment)) {
                        Set<ErpWorkAssignment> workAssignments = new HashSet<>();
                        Set<ErpWorkDay> workDays = new HashSet<>();

                        if(has(detachment.getAssignedEmployees()) && detachment.getAssignedEmployees().size() > 0) {
                            List<ErpEmployee> employees = new ArrayList<>(detachment.getAssignedEmployees());
                            employees.sort(Comparator.comparing(ErpEmployee::getLastname));
                            int x = 11;
                            for(int e = 0; e < employees.size(); e++) {
                                ErpWorkAssignment workAssignment = new ErpWorkAssignment();
                                String employeeId = (String) ExcelUtils.getCellData(x, 19);
                                if(has(employeeId) && !employeeId.isEmpty()) {
                                    workAssignment.setEmployeeAssigned(findEmployeeById(employees, employeeId));
                                } else {
                                    continue;
                                }
                                if(!has(workAssignment.getEmployeeAssigned())) {
                                    continue;
                                }
                                System.out.println(workAssignment.getEmployeeAssigned().getFirstname()+" "+workAssignment.getEmployeeAssigned().getLastname());
                                workAssignment.setWorkSchedule(erpWorkSchedule);
                                String workAssignmentValue = (String) ExcelUtils.getCellData(x, 1);
                                if(has(workAssignmentValue) && !workAssignmentValue.isEmpty()) {
                                    workAssignment.setWorkAssignment(workAssignmentValue);
                                    //System.out.println("Work Assignment Value: "+workAssignmentValue);
                                } else {
                                    workAssignment.setWorkAssignment("SECURITY GUARD");
                                }

                                // generate workdays
                                LocalDate _startDate = convertToLocalDateViaInstant(erpWorkSchedule.getStartDate());
                                LocalDate _stopDate = convertToLocalDateViaInstant(erpWorkSchedule.getStopDate());
                                int y = 2;
                                for (LocalDate date = _startDate; (date.isBefore(_stopDate) || date.isEqual(_stopDate)); date = date.plusDays(1), y++) {
                                    ErpWorkDay workDay = new ErpWorkDay();
                                    workDay.setWorkAssignment(workAssignment);
                                    if(has(detachment.getErpTimeSchedules()) && detachment.getErpTimeSchedules().size() > 0) {
                                        String legendValue = (String) ExcelUtils.getCellData(x, y);
                                        if(has(legendValue) && !legendValue.isEmpty()) {
                                            WorkSchedLegend legend = WorkSchedLegend.valueOf(legendValue);
                                            ErpTimeSchedule timeSchedule = findByLegend(detachment.getErpTimeSchedules(), legend);
                                            LocalDateTime ldt_start = date.atTime(timeSchedule.getStartTime().toLocalTime());
                                            LocalDateTime ldt_stop = date.atTime(timeSchedule.getEndTime().toLocalTime());
                                            workDay.setShiftStart(convertToDateViaInstant(ldt_start));
                                            workDay.setShiftEnd(convertToDateViaInstant(ldt_stop));
                                            workDay.setShiftSchedule(legend);
                                            //System.out.println("ldt_start: "+ldt_start.getDayOfMonth());
                                            //System.out.println("ldt_stop: "+ldt_stop.getDayOfMonth());
                                            System.out.println("WD START: "+ldt_start);
                                            System.out.println("WD STOP: "+ldt_stop);
                                            //System.out.println("WD LEGEND: "+legend.name());
                                        }
                                    }
                                    workDays.add(workDay);
                                }
                                workAssignment.setWorkDays(workDays);
                                workAssignments.add(workAssignment);
                                x++;
                            }

                            erpWorkSchedule.setWorkAssignments(workAssignments);

                            workScheduleService.save(erpWorkSchedule);
                            for(ErpWorkAssignment wa: workAssignments) {
                                //System.out.println("WORK ASSN: "+wa);
                                workAssignmentService.save(wa);
                            }
                            for(ErpWorkDay wd: workDays) {
                                workDayService.save(wd);
                            }

                            addDetailMessage("SUCCESSFUL", file.getFileName() + " is uploaded.", FacesMessage.SEVERITY_INFO);
                        } else {
                            addDetailMessage("FAILED UPLOAD", file.getFileName() + " Detachment has no Assigned employees.", FacesMessage.SEVERITY_ERROR);
                        }
                    } else {
                        addDetailMessage("FAILED UPLOAD", file.getFileName() + " cannot find Detachment.", FacesMessage.SEVERITY_ERROR);
                    }
                } else {
                    addDetailMessage("FAILED UPLOAD", file.getFileName() + " cannot find Stop and Start Date.", FacesMessage.SEVERITY_ERROR);
                }
            }
        } else {
            addDetailMessage("FAILED UPLOAD", "No File Selected.", FacesMessage.SEVERITY_ERROR);
        }
    }

    public ErpTimeSchedule findByLegend(Set<ErpTimeSchedule> erpTimeSchedules, WorkSchedLegend legend) {
        for(ErpTimeSchedule ts: erpTimeSchedules) {
            if(ts.getLegend().equals(legend)) {
                return ts;
            }
        }
        return null;
    }

    public ErpEmployee findEmployeeById(List<ErpEmployee> employees, String employeeId) {
        for(ErpEmployee emp: employees) {
            if(emp.getEmployeeId().equals(employeeId)) {
                return emp;
            }
        }
        return null;
    }

    public void downloadFile(String detachId) throws Exception {
        //System.out.println("DETACHMENT ID: " + detachId);
        if(has(detachId)) {
            Long detId = Long.parseLong(detachId);
            ErpDetachment detachment = erpDetachmentService.findById(detId);
            boolean hasAssignedEmployees = (has(detachment) && detachment.getAssignedEmployees() != null && detachment.getAssignedEmployees().size() > 0);
            boolean hasSchedules = (has(detachment) && detachment.getErpTimeSchedules() != null && detachment.getErpTimeSchedules().size() > 0);
            LocalDate today = LocalDate.now();
            LocalDate startSched = null;
            LocalDate stopSched = null;
            List<String> legends = new ArrayList<>();

            if(today.getDayOfMonth() <= 14) {
                startSched = LocalDate.of(today.getYear(), today.getMonth(), 16);
                LocalDate monthstart = LocalDate.of(today.getYear(),today.getMonth(),1);
                stopSched = monthstart.plusDays(monthstart.lengthOfMonth()-1);
            } else {
                startSched = LocalDate.of(today.getYear(), today.getMonth().plus(1), 1);
                stopSched = LocalDate.of(today.getYear(), today.getMonth().plus(1), 15);
            }

            ExcelUtils.initializeWithFilename("work_schedule.xlsx", "Sheet1");
            // locked style cells
            CellStyle locked = ExcelUtils.workbook.createCellStyle();
            locked.setLocked(true);
            ExcelUtils.setCell(0,1, detachment.getId());
            ExcelUtils.setCell(7,2, "DETACHMENT: "+detachment.getName());
            ExcelUtils.setCell(7,8, "AREA OF RESPONSIBILITY: "+detachment.getLocation().getLocation());
            ExcelUtils.setCell(2,3, "");
            ExcelUtils.setCell(2,8, "");
            ExcelUtils.setCell(2,13, "");
            ExcelUtils.setCell(1,1, startSched.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            ExcelUtils.setCell(2,1, stopSched.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            for(ErpTimeSchedule ts: detachment.getErpTimeSchedules()) {
                if(ts.getLegend().equals(WorkSchedLegend.DS)) {
                    CellStyle quotedPrefix = ExcelUtils.getCellStyle(4,5);
                    quotedPrefix.setQuotePrefixed(true);
                    ExcelUtils.setCell(4, 5, "= Day Shift ("+ts.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))+"-"+ts.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))+")", quotedPrefix);
                    legends.add(WorkSchedLegend.DS.name());
                } else if(ts.getLegend().equals(WorkSchedLegend.NS)) {
                    CellStyle quotedPrefix = ExcelUtils.getCellStyle(5,5);
                    quotedPrefix.setQuotePrefixed(true);
                    ExcelUtils.setCell(5, 5, "= Night Shift ("+ts.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))+"-"+ts.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))+")", quotedPrefix);
                    legends.add(WorkSchedLegend.NS.name());
                } else if(ts.getLegend().equals(WorkSchedLegend.MID)) {
                    ExcelUtils.setCell(4,9,"MID");
                    CellStyle quotedPrefix = ExcelUtils.getCellStyle(4,10);
                    quotedPrefix.setQuotePrefixed(true);
                    ExcelUtils.setCell(4, 10, "= Mid Shift ("+ts.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))+"-"+ts.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))+")", quotedPrefix);
                    legends.add(WorkSchedLegend.MID.name());
                }
            }
            if(has(legends) && legends.size() > 0) {
                legends.add(WorkSchedLegend.DO.name());
            }
            ExcelUtils.evaluateCell(7,0);
            ExcelUtils.evaluateCell(4,1);
            ExcelUtils.evaluateCell(8,0);
            // loop from start date to end date
            int y = 2;
            for (LocalDate date = startSched; (date.isBefore(stopSched) || date.isEqual(stopSched)); date = date.plusDays(1), y++) {
                ExcelUtils.setCell(10, y, date.getDayOfMonth());
            }
            ExcelUtils.setCell(10, 18, "NO. DAYS");
            if(hasAssignedEmployees && hasSchedules){
                int x = 11;
                List<ErpEmployee> employees = new ArrayList<>(detachment.getAssignedEmployees());
                employees.sort(Comparator.comparing(ErpEmployee::getLastname));
                for(ErpEmployee emp: employees) {
                    //System.out.println("EMPLOYEE: "+emp.getFirstname() + " " + emp.getLastname());
                    if(has(emp.getFirstname()) && has(emp.getLastname())) {
                        ExcelUtils.setCell(x, 0, emp.getLastname() + ", " + emp.getFirstname());
                    }
                    if(has(emp.getPosition())) {
                        CellStyle style = ExcelUtils.workbook.createCellStyle();
                        style.setAlignment(HorizontalAlignment.LEFT);
                        ExcelUtils.setCell(x, 1, emp.getPosition(), style);
                    }
                    y = 2;
                    for (LocalDate date = startSched; (date.isBefore(stopSched) || date.isEqual(stopSched)); date = date.plusDays(1), y++) {
                        CellStyle style = ExcelUtils.workbook.createCellStyle();
                        style.setAlignment(HorizontalAlignment.CENTER);
                        ExcelUtils.setCell(x, y, "DS", style);
                        DataValidationHelper dvHelper = ExcelUtils.worksheet.getDataValidationHelper();
                        DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint((has(detachment.getErpTimeSchedules()) && detachment.getErpTimeSchedules().size() > 0) ? legends.toArray(new String[0]) : new String[]{"DS", "NS", "DO"});
                        CellRangeAddressList addressList = new CellRangeAddressList(x, x, y, y);
                        DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
                        validation.setShowErrorBox(true);
                        ExcelUtils.worksheet.addValidationData(validation);
                    }
                    CellStyle style = ExcelUtils.workbook.createCellStyle();
                    style.setAlignment(HorizontalAlignment.LEFT);
                    ExcelUtils.setCell(x, 18, y-2, style);
                    style = ExcelUtils.workbook.createCellStyle();
                    Font font = ExcelUtils.workbook.createFont();
                    font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                    style.setFont(font);
                    ExcelUtils.setCell(x, 19, emp.getEmployeeId(), style);
                    x++;
                }
            }

            String detachmentName = detachment.getName().replaceAll(" ", "_");
            generateFile(ExcelUtils.workbook, "work_schedule_"+detachmentName+today.format(DateTimeFormatter.ofPattern("MMddyyyy")));
            ExcelUtils.workbook.close();
            //FacesContext.getCurrentInstance().getExternalContext().redirect("ws-download-templates.xhtml");
        } else {
            addDetailMessage("DOWNLOAD FAILED", "COULD NOT DOWNLOAD FILE", FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().getExternalContext().redirect("ws-download-templates.xhtml");
        }
    }

    public void generateFile(Workbook workbook, String fileName) throws IOException {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final ExternalContext externalContext = fc.getExternalContext();

        externalContext.responseReset();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment;filename="+fileName+".xlsx");
        final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        final ServletOutputStream out = response.getOutputStream();

        workbook.write(out);
        workbook.close();
        out.flush();
        fc.responseComplete();
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
