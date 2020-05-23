package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sigma.model.Segment;
import com.sigma.model.Acheteur;
import com.sigma.model.Metrique;;


public class SegmentTest {

	@Test
	public void create_Segment_Not_Empty_Without_List() {
		//GIVEN
		Segment segment;

	
		//WHEN
		segment = new Segment("segment", "13","15");
		//THEN
		assertEquals("segment", segment.getLibelle());
		assertEquals("13", segment.getCodeCPV());
		assertEquals("15", segment.getCodeAPE());
	}

	@Test
	public void create_Segment_Not_Empty_with_List(){
		//GIVEN
		Segment segment;
		List<Metrique> metriques= new ArrayList<Metrique>();
		metriques.add(new Metrique("metrique",5));
		//WHEN
		segment = new Segment("segment","13","15",metriques);
		//THEN
		assertEquals("segment", segment.getLibelle());
		assertEquals("13", segment.getCodeCPV());
		assertEquals("15", segment.getCodeAPE());
		assertEquals(metriques, segment.getMetriques());
	}
	@Test
	public void create_segment_Empty() {
		//GIVEN
		Segment segment ;
		//WHEN
		segment = new Segment();
		//THEN
		assertEquals(null, segment.getLibelle());
		assertEquals(null, segment.getCodeAPE());
		assertEquals(null, segment.getCodeAPE());
	}
	@Test
	public void change_Id() {
		//GIVEN
		Segment segment = new Segment();
		Long id = (long) 56;
		//WHEN
		segment.setId(id);
		//THEN
		assertEquals(id, segment.getId());
	}
	@Test
	public void change_Code_CPV() {
		//GIVEN
		Segment segment = new Segment();
		//WHEN
		segment.setCodeCPV("15");
		//THEN
		assertEquals("15", segment.getCodeCPV());
	}
	@Test
	public void change_Code_Ape() {
		//GIVEN
		Segment segment = new Segment();
		//WHEN
		segment.setCodeAPE("13");;
		//THEN
		assertEquals("13", segment.getCodeAPE());
	}
	@Test
	public void test_Add_Metrique() {
		//GIVEN
		Segment segment;
		List<Metrique> metriques= new ArrayList<Metrique>();
		metriques.add(new Metrique("metrique",5));
		segment = new Segment("segment","13","15",metriques);
		Metrique metrique=new Metrique("metrique2",7);
		//WHEN
		segment.addMetrique(metrique);
		//THEN
		assertEquals(true, segment.getMetriques().contains(metrique));
	}
	@Test
	public void test_Remove_Metrique() {
		//GIVEN
		Segment segment;
		List<Metrique> metriques= new ArrayList<Metrique>();
		metriques.add(new Metrique("metrique",5));
		segment = new Segment("segment","13","15",metriques);
		Metrique metrique=new Metrique("metrique2",7);
		segment.addMetrique(metrique);
		//WHEN
		segment.removeMetrique(metrique);

		//THEN
		assertEquals(false, segment.getMetriques().contains(metrique));
	}
	@Test
	public void test_Set_Metrique() {
		//GIVEN
		Segment segment;
		List<Metrique> metriques= new ArrayList<Metrique>();
		List<Metrique> m= new ArrayList<Metrique>();
		metriques.add(new Metrique("metrique",5));
		segment = new Segment("segment","13","15",metriques);
		Metrique metrique=new Metrique("metrique2",7);
		m.add(metrique);

		//WHEN
		segment.setMetrique(m);

		//THEN
		assertEquals(m, segment.getMetriques());
	}
	@Test
	public void setAcheteur() {
		//GIVEN
		Segment segment = new Segment();
		Acheteur acheteur= new Acheteur();
		//WHEN
		segment.setAcheteur(acheteur);
		//Given
		assertEquals(acheteur, segment.getAcheteur());

	}

}
