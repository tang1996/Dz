package com.dz.util;

public class PerformanceAppraisalUtil {
	public String getMark(int total) {
		String result = "";
		if (total >= 30 && total < 50) {
			result = SalerParameterUtils.grade_3;
		} else if (total >= 50 && total < 80) {
			result = SalerParameterUtils.grade_2;
		} else if (total >= 80) {
			result = SalerParameterUtils.grade_1;
		} else {
			result = SalerParameterUtils.grade_4;
		}
		return result;
	}

	public boolean isView(String score, int total) {
		boolean result = false;
		if (SalerParameterUtils.rank_D.equals(score)) {
			System.out.println(SalerParameterUtils.rank_D);
			result = unPass(total);
		}
		if (SalerParameterUtils.rank_C.equals(score)) {
			System.out.println(SalerParameterUtils.rank_C);
			result = pass(total);
		}
		if (SalerParameterUtils.rank_B.equals(score)) {
			System.out.println(SalerParameterUtils.rank_B);
			result = nice(total);
		}
		if (SalerParameterUtils.rank_A.equals(score)) {
			System.out.println(SalerParameterUtils.rank_A);
			result = excellent(total);
		}
		return result;
	}

	public boolean unPass(int total) {
		boolean result = false;
		if (total < 30) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public boolean pass(int total) {
		boolean result = false;
		if (total >= 30 && total < 50) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
	public boolean nice(int total) {
		boolean result = false;
		if (total >= 50 && total < 80) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public boolean excellent(int total) {
		boolean result = false;
		if (total >= 80) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
}
