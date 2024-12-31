package com.ve.inventory_billing.exchange.dto;

import java.util.List;

public class PaginationResponseDto <T> {
    Integer page;
    Integer showRows;
    Integer total;
    Integer offset;
    List<T> data;

    public PaginationResponseDto(
            Integer page,
            Integer showRows,
            Integer total,
            Integer offset,
            List<T> data
    ) {
        this.setShowRows(showRows);
        this.setPage(page);
        this.setTotal(total);
        this.setOffset(offset);
        this.setData(data);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getShowRows() {
        return showRows;
    }

    public void setShowRows(Integer showRows) {
        this.showRows = showRows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
