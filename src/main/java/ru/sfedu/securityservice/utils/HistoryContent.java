package ru.sfedu.securityservice.utils;

import ru.sfedu.securityservice.models.enums.Outcomes;

import java.util.Date;
import java.util.Objects;

public class HistoryContent {
    private String className;
    private Date createdDate;
    private String actor;
    private String methodName;
    private Object object;
    private Outcomes outcomes;

    public HistoryContent() {
    }

    public HistoryContent(String className, Date createdDate, String actor) {
        this.className = className;
        this.createdDate = createdDate;
        this.actor = actor;
    }

    public HistoryContent(String className, Date createdDate, String actor, String methodName, Object object, Outcomes outcomes) {
        this.className = className;
        this.createdDate = createdDate;
        this.actor = actor;
        this.methodName = methodName;
        this.object = object;
        this.outcomes = outcomes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Outcomes getStatus() {
        return outcomes;
    }

    public void setStatus(Outcomes status) {
        this.outcomes = outcomes;
    }

    @Override
    public boolean equals(Object o) { // равенство объектов класса
        if (this == o) return true;// сравниваем 2 объекта одного класса(объект эквивалентен самому себе)
        if (o == null || getClass() != o.getClass()) return false;//не равно null
        HistoryContent that = (HistoryContent) o;
        return Objects.equals(className, that.className) && Objects.equals(createdDate, that.createdDate) && Objects.equals(actor, that.actor) && Objects.equals(methodName, that.methodName) && Objects.equals(object, that.object) && outcomes == that.outcomes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, createdDate, actor, methodName, object, outcomes);
    }// сравение объектов

    @Override
    public String toString() {
        return "HistoryContent{" +
                "className='" + className + '\'' +
                ", createdDate=" + createdDate +
                ", actor='" + actor + '\'' +
                ", methodName='" + methodName + '\'' +
                ", object=" + object +
                ", outcomes=" + outcomes +
                '}';
    }
}
