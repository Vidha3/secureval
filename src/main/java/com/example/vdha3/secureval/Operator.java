package com.example.vdha3.secureval;

import java.util.*;
import java.io.*;

public abstract class Operator{
	public Variable first;
	public Variable second;
	public String token;

	abstract void op();
}