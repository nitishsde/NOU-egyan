package com.lmsapp.lms.controllar;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.hibernate.sql.exec.spi.StandardEntityInstanceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsapp.lms.dto.MaterialDto;
import com.lmsapp.lms.model.Enquiry;
import com.lmsapp.lms.model.Material;
import com.lmsapp.lms.model.Response;
import com.lmsapp.lms.model.StudentInfo;
import com.lmsapp.lms.service.EnquiryRepo;
import com.lmsapp.lms.service.MaterialRepo;
import com.lmsapp.lms.service.ResponseRepo;
import com.lmsapp.lms.service.StudentInfoRepo;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminControllar {
	
	private static final CrudRepository<Enquiry, Integer> stdrepo = null;

	@Autowired
	StudentInfoRepo srepo;
	
	@Autowired
	EnquiryRepo eqrepo;
	
	@Autowired
	ResponseRepo resrepo;
	
	@Autowired
	MaterialRepo mrepo;
 

	
	//Admin dashboard.. start
	@GetMapping("/adhome")
	public String showAdminHome(HttpSession session, HttpServletResponse response, Model model)
	{

		try {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid")!=null) {
				long scount= srepo.count();
				model.addAttribute("scount", scount);
				
				return "admin/adminhome";
			}
			else {
				return "redirect:/adminlogin";
			}
			
		} catch (Exception e) {
			return "redirect:/adminlogin";
		}
		
	}
	

	//Admin dashboard.. end
	
	
	@GetMapping("/logout")
	public String Logout(HttpSession session)
	{
		session.invalidate();
		return "redirect:/adminlogin";
	}
	
	@GetMapping("/viewstudent")
	public String showViewStudent(HttpSession session, HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid")!=null) {
				List<StudentInfo> stu=srepo.findAll();
				model.addAttribute("stu", stu);
				return "admin/viewstudent";
			}
			else {
				return "redirect:/adminlogin";
			}
			
		} catch (Exception e) {
			return "redirect:/adminlogin";
		}
		
	}
	
	@GetMapping("/viewenquiry")
	public String showViewEnquiry(HttpSession session, HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid")!=null) {
				List<Enquiry> eqList = eqrepo.findAll();
				model.addAttribute("eqlist", eqList);
				return "admin/viewenquiry";
			}
			else {
				return "redirect:/adminlogin";
			}
			
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
		
	}
	
	
	
	@GetMapping("/viewcomplaint")
	public String ShowViewComplaint(HttpSession session, HttpServletResponse response, Model model)
	{
		try 
		{
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		if(session.getAttribute("adminid")!=null)
		{
			List<Response> clist = resrepo.FindResponseByResponseType("Complaint");
			model.addAttribute("clist", clist);
			return "admin/viewcomplaint";
		}
		else
		{
			return "redirect:/adminlogin";
		}
		}
		catch(Exception ex)
		{
			return "redirect:/adminlogin";
		}
	}
	
	
	@GetMapping("/viewfeedback")
	public String showViewFeedback(HttpSession session, HttpServletResponse response, Model model)
	{
	 try {
		 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		 if (session.getAttribute("adminid")!=null) 
		 {
			List<Response> flist = resrepo.FindResponseByResponseType("Feedback");
			model.addAttribute("flist", flist);
			return "admin/viewfeedback";
			
		}
		 else {
			return "redirect:/adminlogin";
		}
		
	} catch (Exception e) {
		return "redirect:/adminlogin";
	}	
	}
	
	@GetMapping("/viewfeedback/delate")
	public String DeleteFeedback(@RequestParam int resid, HttpSession session,Model model,RedirectAttributes redirectAttributes)
	{
		try {
			if(session.getAttribute("adminid")!=null)
			{
				Response res = resrepo.findById(resid).get();
				resrepo.delete(res);
				redirectAttributes.addFlashAttribute("msg", resid+"is deleted successfully");
				return "redirect:/admin/viewfeedback";
			}
			else {
				return "redirect:/adminlogin";
			}
			
		} catch (Exception ex) {
			return "redirect:/adminlogin";
		}
	}
     
	@GetMapping("/addmaterial")
	public String showAddMaterial(HttpSession session, HttpServletResponse response, Model model)
	{
	try 
	{
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	if(session.getAttribute("adminid")!=null)
	{
		MaterialDto dto=new MaterialDto();
		model.addAttribute("dto", dto);
		return "admin/addmaterial";
	}
	else
	{
		return "redirect:/adminlogin";
	}
	}
	catch(Exception ex)
	{
		return "redirect:/adminlogin";
	}
	}
	
	
	
	@PostMapping("/addmaterial")
	public String CreateMaterial(HttpSession session, HttpServletResponse response, @ModelAttribute MaterialDto dto , RedirectAttributes attrib)
	{
	try 
	{
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	if(session.getAttribute("adminid")!=null)
	{
	    MultipartFile filedata=dto.getFiledata();
	    String storageFileName=new Date().getTime()+"_"+filedata.getOriginalFilename();
	    String uploadDir="public/mat/";
	    Path uploadPath=Paths.get(uploadDir);
	    if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
	    try(InputStream inputStream=filedata.getInputStream()){
	    	Files.copy(inputStream, Paths.get(uploadDir+storageFileName), StandardCopyOption.REPLACE_EXISTING);
	    }
	    Material m= new Material();
	    m.setProgram(dto.getProgram());
	    m.setBranch(dto.getBranch());
	    m.setYear(dto.getYear());
	    m.setMaterialtype(dto.getMaterialtype());
	    m.setSubject(dto.getSubject());
	    m.setTopic(dto.getTopic());
	    m.setFilename(storageFileName);
	    m.setPosteddate(new Date()+"");
	    mrepo.save(m);
	    attrib.addFlashAttribute("msg", "Material is Added");
	    
		return "redirect:/admin/addmaterial";
	}
	else
	{
		return "redirect:/adminlogin";
	}
	}
	catch(Exception ex)
	{
		return "redirect:/adminlogin";
	}
	}
	
	@GetMapping("/viewstudymaterial")
	public String ViewStudyMaterial(HttpSession session, HttpServletResponse response,Model model)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid")!=null) {
				List<Material> mlist=mrepo.findAll();
				model.addAttribute("mlist", mlist);
				return "admin/viewstudymaterial";
			}
			else {
				return "redirect:/adminlogin";
			}
			
		} catch (Exception e) {
			return "redirect:/adminlogin";
		}
		
	}
	
	@GetMapping("/viewstudymaterial/deletematerial")
	public String DeleteMaterial(HttpSession session, HttpServletResponse response, @RequestParam int id)
	{
		try {
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (session.getAttribute("adminid")!=null) {
				Material m=mrepo.getById(id);
				Path filePath=Paths.get("public/mat/"+m.getFilename());
				try {
					Files.delete(filePath);
					
				} catch (Exception ex) {
				    ex.	printStackTrace();
				}
				mrepo.delete(m);
				return "redirect:/admin/viewstudymaterial";
			}
			else {
				return "redirect:/adminlogin";
			}
			
		} catch (Exception e) {
			return "redirect:/adminlogin";
		}
		
	}
	
	
}

	