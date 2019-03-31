package com.ynet.securitiessystem.model;

import java.io.Serializable;
import java.util.List;

public class ProductGroup implements Serializable {

    private static final long serialVersionUID = 8729495986418512315L;
    private String groupName;

    private List<Bond> productList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Bond> getProductList() {
        return productList;
    }

    public void setProductList(List<Bond> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "ProductGroup{" +
                "groupName='" + groupName + '\'' +
                ", productList=" + productList +
                '}';
    }
}
