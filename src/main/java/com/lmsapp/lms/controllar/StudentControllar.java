package com.lmsapp.lms.controllar;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsapp.lms.dto.ResponseDto;
import com.lmsapp.lms.dto.StudentInfoDto;
import com.lmsapp.lms.model.Material;
import com.lmsapp.lms.model.Response;
import com.lmsapp.lms.model.StudentInfo;
import com.lmsapp.lms.service.MaterialRepo;
import com.lmsapp.lms.service.ResponseRepo;
import com.lmsapp.lms.service.StudentInfoRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentControllar {
	
	@Autowired
	StudentInfoRepo srepo;
	
	@Autowired
	ResponseRepo resrepo;
	
	@Autowired
	MaterialRepo mrepo;
	
	@GetMapping("/home")
	public String showStudentHome(HttpSession session, HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if(session.getAttribute("studentid")!=null) {
				StudentInfo sinfo = srepo.findById(session.getAttribute("studentid").toString()).get();
				model.addAttribute("sinfo", sinfo);
				
				StudentInfoDto dto = new StudentInfoDto();
				model.addAttribute("dto", dto);
				return "student/studenthome";
			}
			else {
				return "redirect:/studentlogin";
			}
			
		} catch (Exception ex) {
			return "redirect:/studentlogin";
		} 
	}
	
	//Student Dashboard start---------
	
	@PostMapping("/home")
	public String UploadPic(HttpSession session,RedirectAttributes redirectAttributes,@ModelAttribute StudentInfoDto studentInfoDto)
	{
		if (session.getAttribute("studentid")!=null) {
			
			try {
			      MultipartFile filedata =studentInfoDto.getProfilepic();
			      String storagefileName = new Date().getTime()+"_"+filedata.getOriginalFilename();
			      String uploadDir = "public/user/";
			      Path uoloadPath = Paths.get(uploadDir);
			      if(!Files.exists(uoloadPath));
			      {
			    	  Files.createDirectories(uoloadPath);
			      }
			      
			      try(InputStream inputStream = filedata.getInputStream())
			      {
			    	  Files.copy(inputStream, Paths.get(uploadDir+storagefileName), StandardCopyOption.REPLACE_EXISTING);
			      }
			      
			     StudentInfo s = srepo.findById(session.getAttribute("studentid").toString()).get();
			      s.setProfilepic(storagefileName);
			      srepo.save(s);
			      redirectAttributes.addFlashAttribute("msg", "Profile  Uploded successfully");
				
				return "redirect:/student/home";
				
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("msg", "Something went wrong"+e.getMessage());
				return "redirect:/student/home";
			}
		}
		else {
			return "redirect:/studentlogin";
		}
		
		
	}
	//Student dashboard end----
	
	
	@GetMapping("/studymaterial")
	public String showStudyMaterial(HttpSession session ,Model model)
	{
	try {
		
		if(session.getAttribute("studentid")!=null) {
			StudentInfo s=srepo.getById(session.getAttribute("studentid").toString());
			String program=s.getProgram();
			String branch=s.getBranch();
			String year=s.getYear();
			String materialtype="smat";
			List<Material> mlist=mrepo.getMaterial(program, branch,year,materialtype);
			model.addAttribute("mlist", mlist);
		return "student/viewstudymaterial";
		}
		
		else {
			return "redirect:/studentlogin";
		}
		
	} catch (Exception ex) {
		return "redirect:/studentlogin";
		
	}
	}
	
	
	
	@GetMapping("/viewassignment")
	public String showViewAssignment(HttpSession session,Model model)
	{
		try {
			
			if(session.getAttribute("studentid")!=null) {
				StudentInfo s=srepo.getById(session.getAttribute("studentid").toString());
				String program=s.getProgram();
				String branch=s.getBranch();
				String year=s.getYear();
				String materialtype="assign";
				List<Material> mlist=mrepo.getMaterial(program,branch,year,materialtype);
				model.addAttribute("mlist", mlist);
	            return "student/viewassignment";
			}
			else {
				return "redirect:/studentlogin";
			}
			
		} catch (Exception ex) {
			return "redirect:/studentlogin";
		}
	}
	
	@GetMapping("/giveresponse")
	public String showGiveResponse(HttpSession session, HttpServletResponse response, Model model, ResponseDto dto)
	{
		response.setHeader("cache-control", "no-cache,no-store,must-revalidate");
		try {
			if(session.getAttribute("studentid")!=null) {
				ResponseDto dt= new ResponseDto();
				model.addAttribute("dto", dto);
				
				return "student/giveresponse";
			}
			else {
				return "student:/giveresponse";
			}
			
		} catch (Exception ex) {
			return "student:/studentlogin";
		}
	}
	
	

	
	@GetMapping("/changepassword")
	public String showChangePassword(HttpSession session) {
		try {
			if (session.getAttribute("studentid")!=null) {
				return "student/changepassword";
			}
			else {
				return "redirect:/studentlogin" ;
			}
			
		} catch (Exception ex) {
		  return "redirect:/studentlogin" ;
		}
	}

	@GetMapping("/logout")
	public String Logout(HttpSession session)
	{
		session.invalidate();
		return "redirect:/studentlogin";
	}


@PostMapping("/changepassword")
public String ShowChangePassword(HttpSession session, HttpServletResponse response, HttpServletRequest request, RedirectAttributes attrib)
{
	try {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		if(session.getAttribute("studentid")!=null) {
			StudentInfo s=srepo.getById(session.getAttribute("studentid").toString());
			String oldpassword=request.getParameter("oldpassword");
			String newpassword=request.getParameter("newpassword");
			String confirmpassword=request.getParameter("confirmpassword");
			if (!newpassword.equals(confirmpassword)) 
			{
				attrib.addFlashAttribute("msg", "Newpasswor & Confirmpassword Not Matched");
				return "redirect:/student/changepassword";
			}
			if (!oldpassword.equals(s.getPassword()))
			{
				attrib.addFlashAttribute("msg", "Oldpassword is not matched");
				return "redirect:/student/changepassword";
			}
			s.setPassword(newpassword);
			srepo.save(s);
			return "redirect:/student/logout";
		}
		else {
			return "redirect:/studentlogin";
		}
		
	} catch (Exception ex) {
		return "redirect:/studentlogin";
	} 
}

@PostMapping("/giveresponse")
public String submitGiveResponse(HttpSession session, @ModelAttribute ResponseDto responseDto, Model model, RedirectAttributes redirectAttributes,HttpServletResponse response)
{
	try {
		if(session.getAttribute("studentid")!=null)
		{
			StudentInfo std = srepo.getById(session.getAttribute("studentid").toString());
			model.addAttribute("studentid", session.getAttribute("userid"));
			
			Response res = new Response();
			
			res.setName(std.getName());
			res.setEnrollmentno(std.getEnrollmentno());
			res.setEmailaddress(std.getEmailaddress());
			res.setContactno(std.getContactno());
			res.setResponsetype(responseDto.getResponsetype());
			res.setSubject(responseDto.getSubject());
			res.setMessage(responseDto.getMessage());
			res.setResdate(new Date()+"");
			resrepo.save(res);
			
			return "redirect:/student/giveresponse";
			
		}
		else {
			return "redirect:/stuent/giveresponse";
		}
		
	} catch (Exception ex) {
	 return  "redirect:/studentlogin";
	}
	
	
}

}