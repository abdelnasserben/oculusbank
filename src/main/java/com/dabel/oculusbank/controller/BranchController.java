package com.dabel.oculusbank.controller;

import com.dabel.oculusbank.app.util.StatedObjectFormatter;
import com.dabel.oculusbank.app.web.Endpoint;
import com.dabel.oculusbank.app.web.PageTitleConfig;
import com.dabel.oculusbank.app.web.View;
import com.dabel.oculusbank.constant.web.CurrentPageTitle;
import com.dabel.oculusbank.constant.web.MessageTag;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.service.delegate.DelegateBranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BranchController implements PageTitleConfig {

    @Autowired
    DelegateBranchService delegateBranchService;

    @GetMapping(value = Endpoint.Branch.ROOT)
    public String branches(Model model, BranchDTO branchDTO) {

        setTitleAndAddListOfALlBranchesAttribute(model);
        return View.Branch.ROOT;
    }

    @PostMapping(value = Endpoint.Branch.ROOT)
    public String addNewBranch(Model model, @Valid BranchDTO branchDTO, BindingResult binding,
                               @RequestParam(required = false, defaultValue = "0") double assetKMF,
                               @RequestParam(required = false, defaultValue = "0") double assetEUR,
                               @RequestParam(required = false, defaultValue = "0") double assetUSD,
                               RedirectAttributes redirect) {

        if(binding.hasErrors() || assetKMF < 0 || assetEUR < 0 || assetUSD < 0) {
            setTitleAndAddListOfALlBranchesAttribute(model);
            model.addAttribute(MessageTag.ERROR, "Invalid information !");
            return View.Branch.ROOT;
        }

        double[] vaultsAssets = new double[]{assetKMF, assetEUR, assetUSD};
        delegateBranchService.create(branchDTO, vaultsAssets);
        redirect.addFlashAttribute(MessageTag.SUCCESS, "New branch added successfully !");

        return "redirect:" + Endpoint.Branch.ROOT;
    }

    private void setTitleAndAddListOfALlBranchesAttribute(Model model) {
        setPageTitle(model, "Branches", "Settings");
        model.addAttribute("branches", StatedObjectFormatter.format(delegateBranchService.findAll()));
    }

    @Override
    public void setPageTitle(Model model, String pageTitle, String breadcrumb) {

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("pageBreadcrumb", breadcrumb);
        model.addAttribute("currentPage", CurrentPageTitle.SETTINGS);
    }
}
