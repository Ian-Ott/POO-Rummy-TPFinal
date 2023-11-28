package ar.edu.unlu.poo.Serializacion.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AddableObjectOutputStream extends ObjectOutputStream {
	/** Constructor que recibe OutputStream */
	public AddableObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	/** Constructor sin parametros */
	protected AddableObjectOutputStream() throws IOException, SecurityException {
		super();
	}

	/** Redefinicion del metodo de escribir la cabecera para que no haga nada. */
	protected void writeStreamHeader() throws IOException {
	}

}
