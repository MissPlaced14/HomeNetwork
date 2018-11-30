package com.example.rajatkumar.homenetwork;

public class Device {
int id;
String mac;
String tunPas;
String op;
String pass;

public Device(int id, String mac, String tunPas, String op, String pass) {
	this.id = id;
	this.mac = mac;
	this.tunPas = tunPas;
	this.op = op;
	this.pass = pass;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getMac() {
	return mac;
}
public void setMac(String mac) {
	this.mac = mac;
}
public String getTunPas() {
	return tunPas;
}
public void setTunPas(String tunPas) {
	this.tunPas = tunPas;
}
public String getOp() {
	return op;
}
public void setOp(String op) {
	this.op = op;
}
public String getPass() {
	return pass;
}
public void setPass(String pass) {
	this.pass = pass;
}
}
