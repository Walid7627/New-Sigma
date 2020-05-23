package Dto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sigma.dto.SegmentDto;
import com.sigma.model.Acheteur;
import com.sigma.model.Metrique;
import com.sigma.model.Segment;

public class SegmentDtoTest {

	@Test
	public void create_segmentDto_Empty() {
		//GIVEN
		SegmentDto segmentDto ;
		//WHEN
		segmentDto = new SegmentDto();
		//THEN
		assertEquals(null, segmentDto.getLibelle());
		assertEquals(null, segmentDto.getCodeAPE());
		assertEquals(null, segmentDto.getCodeAPE());
	}
	@Test
	public void change_Id() {
		//GIVEN
		SegmentDto segmentDto = new SegmentDto();
		Long id = (long) 56;
		//WHEN
		segmentDto.setId(id);
		//THEN
		assertEquals(id, segmentDto.getId());
	}
	@Test
	public void change_Code_CPV() {
		//GIVEN
		SegmentDto segmentDto = new SegmentDto();
		//WHEN
		segmentDto.setCodeCPV("15");
		//THEN
		assertEquals("15", segmentDto.getCodeCPV());
	}
	@Test
	public void change_Code_Ape() {
		//GIVEN
		SegmentDto segmentDto = new SegmentDto();
		//WHEN
		segmentDto.setCodeAPE("13");;
		//THEN
		assertEquals("13", segmentDto.getCodeAPE());
	}
	@Test
	public void test_Set_Metrique() {
		//GIVEN
		SegmentDto segmentDto;
		List<Metrique> metriques= new ArrayList<Metrique>();
		List<Metrique> m= new ArrayList<Metrique>();
		metriques.add(new Metrique("metrique",5));
		segmentDto = new SegmentDto();
		Metrique metrique=new Metrique("metrique2",7);
		m.add(metrique);

		//WHEN
		segmentDto.setMetriques(m);

		//THEN
		assertEquals(m, segmentDto.getMetriques());
	}


}
