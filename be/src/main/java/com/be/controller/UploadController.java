package com.be.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.be.entity.AllPlan;
import com.be.excelReader.Excel2007Reader;

@CrossOrigin(origins = { "*", "null" })
@Controller
@RequestMapping("/upload")
public class UploadController {

	@RequestMapping("/excel")
	public String planInfo(HttpServletRequest req, Model model) {
		String id = req.getParameter("id");
		String userid = req.getParameter("userid");

		return "excel";
	}
}
