package com.shopping.service;

import java.util.List;

import com.shopping.entity.Home;
import com.shopping.entity.HomeCategory;

public interface HomeService {

	public Home createHomePageData(List<HomeCategory> allCategories);
}
