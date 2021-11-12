package com.metrics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@RestController
@RequestMapping("/course/metrics")
@Timed("course.timeClass") // Metrica a nivel de clase, solo se puede utilizar en metodos web
public class TestMetricsController {

	@Autowired
	private MeterRegistry registry;

	
	private static final Logger log = LoggerFactory.getLogger(TestMetricsController.class);

	/**
	 * Crear una metrica propia. Para iniciar se debe consultar el ws
	 * http://localhost:8080/actuator/metrics/course.metrics
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<String> get() {
		registry.counter("course.metrics").increment();
		return new ResponseEntity<>("@raidentrance", HttpStatus.OK);
	}
	
	/** Medida de tiempo
	 * http://localhost:8080/actuator/metrics/course.timer
	 * 
	 * @return
	 */
	@GetMapping(path = "/time")
	public ResponseEntity<String> getTime() {
		log.info("Metrica de tiempo");
		
		Timer timer = registry.timer("course.timer");
		timer.record(()->{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		return new ResponseEntity<>("Metrica de tiempo", HttpStatus.OK);
	}
	
	@GetMapping(path = "/timeTwo")
	@Timed("course.timeMethod")
	public ResponseEntity<String> getTimeForMethod() {
		log.info("Metrica de tiempo a todo el bloque de codigo");
		
		return new ResponseEntity<>("Metrica de tiempo", HttpStatus.OK);
	}
}
