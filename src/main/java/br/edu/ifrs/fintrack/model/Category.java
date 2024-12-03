package br.edu.ifrs.fintrack.model;

import br.edu.ifrs.fintrack.exception.MissingRequiredFieldException;

public class Category {
    private int id;
    private String type;
    private String name;
    private String icon;

    public Category(int id, String type, String name, String icon) {
        if(type == null || type.isEmpty()) {
            throw new MissingRequiredFieldException("type");
        }

        if(name == null || name.isEmpty()) {
            throw new MissingRequiredFieldException("name");
        }

        this.id = id;
        this.type = type;
        this.name = name;
        this.icon = icon;
    }

    public Category(String type, String name, String icon) {
        if(type == null || type.isEmpty()) {
            throw new MissingRequiredFieldException("type");
        }

        if(name == null || name.isEmpty()) {
            throw new MissingRequiredFieldException("name");
        }

        this.type = type;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if(type == null || type.isEmpty()) {
            throw new MissingRequiredFieldException("type");
        }
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty()) {
            throw new MissingRequiredFieldException("name");
        }
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}

