package com.levchenko.security.model;

public class Student
{
	private int id;
	private String name;
	private int marks;

	public Student(final int id, final String name, final int marks)
	{
		this.id = id;
		this.name = name;
		this.marks = marks;
	}

	public int getId()
	{
		return id;
	}

	public void setId(final int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public int getMarks()
	{
		return marks;
	}

	public void setMarks(final int marks)
	{
		this.marks = marks;
	}
}
