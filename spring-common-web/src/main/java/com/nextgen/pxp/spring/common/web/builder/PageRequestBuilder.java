// Copyright 2019-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.web.builder;

import com.nextgen.pxp.spring.common.web.interfaces.ColumnRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder creating Spring PageRequest based on RESTful pagination and sorting inputs.
 */
public class PageRequestBuilder<E extends Enum<E> & ColumnRepresentation> {

	private int page;
	private final int perPage;
	private final List<Sort.Order> sort = new ArrayList<>();

	/**
	 * @param page       Number of page (0 and 1 specifies first page)
	 * @param sortString String containing one or more column names delimited by "," sign where order of the columns specifies sorting order. If there is "-"
	 *                   sign before the column name, sorting by that column is descending (i.e. "-date,name,-type"). Column names need to match case
	 *                   insensitive values from provided enumClass.
	 * @param enumClass  enum that represents columns that can be used in sort
	 */
	public PageRequestBuilder(int page, int perPage, String sortString, Class<E> enumClass) {
		setPage(page);
		this.perPage = perPage;
		setSort(sortString, enumClass);
	}

	public PageRequest build() {
		return PageRequest.of(page, perPage, Sort.by(sort));
	}

	private void setPage(int page) {
		this.page = page > 0 ? page - 1 : page;
	}

	private void setSort(String sortString, Class<E> enumClass) {
		if (StringUtils.isBlank(sortString)) {
			return;
		}

		for (String sortSubString : sortString.split(",")) {
			Sort.Direction direction = sortSubString.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
			if (Sort.Direction.DESC.equals(direction)) {
				sortSubString = sortSubString.substring(1);
			}
			this.sort.add(new Sort.Order(direction, getEnumValue(sortSubString, enumClass).getColumn()));
		}
	}

	private E getEnumValue(String enumValue, Class<E> enumClass) {
		try {
			return Enum.valueOf(enumClass, enumValue.toUpperCase());
		} catch (Exception e) {
			throw new IllegalArgumentException("Sorting by '" + enumValue + "' not allowed");
		}
	}
}