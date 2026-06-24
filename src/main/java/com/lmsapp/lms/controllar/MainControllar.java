package com.lmsapp.lms.controllar;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsapp.lms.api.SmsSender;
import com.lmsapp.lms.dto.AdminLoginDto;
import com.lmsapp.lms.dto.EnquiryDto;
import com.lmsapp.lms.dto.StudentInfoDto;
import com.lmsapp.lms.model.AdminLogin;
import com.lmsapp.lms.model.Enquiry;
import com.lmsapp.lms.model.StudentInfo;
import com.lmsapp.lms.service.AdminLoginRepo;
import com.lmsapp.lms.service.EnquiryRepo;
import com.lmsapp.lms.service.StudentInfoRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;



@Controller
public class MainControllar {
	
	@Autowired
	EnquiryRepo erepo;
	
	@Autowired
	StudentInfoRepo stdrepo;
	
	@Autowired
	AdminLoginRepo adrepo;

	@GetMapping("/home")
public String ShowIndex()
{
  return "index";
}
	@GetMapping("/aboutus")
	public String ShowAboutUs()
	{
		return "about us";
	}
	
	@GetMapping("/service")
	public String ShowService()
	{
		return "service";
	}
	
	
	
	@GetMapping("/contact")
	public String ShowContact()
	{
		return "contact";
	}
	
	
	@GetMapping("/login")
	public String Showlogin()
	{
		return "login";
	}
	@GetMapping("/ragistration")
	public String ShowRagistration(Model model)
	{
		StudentInfoDto dto = new StudentInfoDto();
		model.addAttribute("dto", dto);
		return "ragistration";
	}
	
	@PostMapping("/ragistration")
	public String Registration(@ModelAttribute StudentInfoDto studentInfoDto,RedirectAttributes redirectAttributes)
	{
		try {
			
			StudentInfo std = new StudentInfo();
			std.setEnrollmentno(studentInfoDto.getEnrollmentno());
			std.setName(studentInfoDto.getName());
			std.setFname(studentInfoDto.getFname());
			std.setMname(studentInfoDto.getMname());
			std.setGender(studentInfoDto.getGender());
			std.setAddress(studentInfoDto.getAddress());
			std.setProgram(studentInfoDto.getProgram());
			std.setBranch(studentInfoDto.getBranch());
			std.setYear(studentInfoDto.getYear());
			std.setContactno(studentInfoDto.getContactno());
			std.setEmailaddress(studentInfoDto.getEmailaddress());
			std.setPassword(studentInfoDto.getPassword());
			std.setRegdate(new Date()+"");
			stdrepo.save(std);
			redirectAttributes.addFlashAttribute("message", "Registration successful");
			
			return "redirect:/ragistration";
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "something went wrong"+e.getMessage());
			return "redirect:/ragistration";
		}
	}
	
	@GetMapping("/studentlogin")
	public String ShowStudentlogin(Model model)
	{
		StudentInfoDto dto=new StudentInfoDto();
		model.addAttribute("dto", dto);
		return "studentlogin";
	}
	
	@PostMapping("/studentlogin")
	public String validateStudent(@ModelAttribute StudentInfoDto dto, HttpSession session,RedirectAttributes attrib)
	{
		try {
			StudentInfo s=stdrepo.getById(dto.getEnrollmentno());
			if(s.getPassword().equals(dto.getPassword())) {
				//attrib.addFlashAttribute("message", "Valid User");
				session.setAttribute("studentid", s.getEnrollmentno());
				return "redirect:/student/home";
			}
			else {
				attrib.addFlashAttribute("message", "Invalid User");
			}
			return "redirect:/studentlogin";
			
		} catch (EntityNotFoundException ex) {
			attrib.addFlashAttribute("message", "Student doesn't exist");
			return "redirect:/studentlogin";
			
		}
	}
	
	@GetMapping("/adminlogin")
	public String ShowAdminlogin(Model model)
	{
		AdminLoginDto dto =new AdminLoginDto();
		model.addAttribute("dto", dto);
		return "adminlogin";
	}
	
	@PostMapping("/adminlogin")
	public String Adminlogin(@ModelAttribute AdminLoginDto adminLoginDto, HttpSession session,RedirectAttributes redirectAttributes)
	{
		try {
			AdminLogin admin = adrepo.getById(adminLoginDto.getUserid());
			if (admin.getPassword().equals(adminLoginDto.getPassword())) {
				//redirectAttributes.addFlashAttribute("msg", "Valid User");
				session.setAttribute("adminid", adminLoginDto.getUserid());
				 return "redirect:/admin/adhome";
			}
			else {
				redirectAttributes.addFlashAttribute("msg", "Invalid user");
				 return "redirect:/adminlogin"	;
			}
			
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("msg", "User does Not Exist");
		 return "redirect:/adminlogin"	;
		}
		
	}
	
	
	@GetMapping("/contactus")
	public String ShowContactus(Model model)
	{
		EnquiryDto dto=new EnquiryDto();
        model.addAttribute("dto", dto);
		return "contactus";
	}
	
	@PostMapping("/contactus")
  public String SubmitEnquiry(@ModelAttribute EnquiryDto enquiryDto,BindingResult result , RedirectAttributes redirectAttributes)
	{
   try {
	   
	   Enquiry eq=new Enquiry();
	   eq.setName(enquiryDto.getName());
	   eq.setGender(enquiryDto.getGender());
	   eq.setContactno(enquiryDto.getContactno());
	   eq.setEmailaddress(enquiryDto.getEmailaddress());
	   eq.setEnquirytext(enquiryDto.getEnquirytext());
	   eq.setPosteddate(new Date()+"");
	   erepo.save(eq);
	   SmsSender ss=new SmsSender();
	   ss.sendSms(enquiryDto.getContactno());
	   redirectAttributes.addFlashAttribute("message", "Form Submited Successfully");
	   return "redirect:/contactus";
			   
   } catch (Exception e) {
	   redirectAttributes.addFlashAttribute("message", "Something went wrong");
	   return "redirect:/contactus";

	}
	}
}
