package ru.sfedu.securityservice.utils;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
@Root(name = "list")
public class Wrapper<T> {
    @ElementList(inline = true, required = false)
    private List<T> list;

    public Wrapper() {
    }

    public Wrapper(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
