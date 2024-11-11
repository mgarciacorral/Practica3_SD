package es.uva.hilos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

public class Centralita {
	private final ArrayList<Empleado> empleados = new ArrayList<>();
	private final Queue<Llamada> llamadas = new LinkedList<>();

	public void conEmpleado(Empleado e) {
		empleados.add(e);
		e.setDisponible(true);
		sortEmpleados();
	}

	public void sortEmpleados(){
		empleados.sort((e1, e2) -> e1.getPrioridad() - e2.getPrioridad());
	}

	public Thread atenderLlamadaConEmpleado(Empleado empleado, Llamada llamada){
		return new Thread(() -> {
			try {
				empleado.atenderLlamada(llamada);
				comprobarLLamadas();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	public void comprobarLLamadas(){
		if(llamadas.size() > 0){
			for(Empleado empleado : empleados){
				if(empleado.isDisponible()){
					Llamada llamada = llamadas.poll();
					empleado.setDisponible(false);
					atenderLlamadaConEmpleado(empleado, llamada).start();
					break;
				}
			}
		}
	}

	public void atenderLlamada(Llamada llamada){
		// TODO: Este método debería seleccionar un empleado disponible según prioridad
		// y correr en un nuevo hilo atenderLlamadaConEmpleado.
		// Este método no bloquea la ejecución si hay empleados disponibles para atender la llamada
		// si no hay empleados disponibles tendremos que esperar a que haya uno.

		llamadas.add(llamada);
		comprobarLLamadas();
	}
}
