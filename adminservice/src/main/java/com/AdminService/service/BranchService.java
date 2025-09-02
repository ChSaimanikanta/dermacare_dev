package com.AdminService.service;

import com.AdminService.dto.BranchDTO;
import com.AdminService.util.Response;

public interface BranchService {
	public Response createBranch(BranchDTO branch);
	Response getBranchById(String branchId);
	Response updateBranch(String branchId, BranchDTO branch);
	Response deleteBranch(String branchId);
	Response getAllBranches();
}
