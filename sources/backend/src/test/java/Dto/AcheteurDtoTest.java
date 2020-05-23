package Dto;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sigma.dto.AcheteurDto;
import com.sigma.model.Equipe;

public class AcheteurDtoTest {

	@Test
	public void test_Acheteur_Empty() {
		//Given
		AcheteurDto acheteurDto ;
		//WHEN
		acheteurDto = new AcheteurDto();
		//THAN
		assertEquals(false, acheteurDto.equals(null));
	}
	@Test
	public void test_Set_Equipe() {
		//Given
		AcheteurDto acheteurDto = new AcheteurDto() ;
		Equipe equipe = new Equipe();
		//WHEN
		acheteurDto.setEquipe(equipe);
		//THAN
		assertEquals(true,acheteurDto.getEquipe().equals(equipe));
				
	}
	@Test
	public void test_Set_Segment() {
		//Given
		AcheteurDto acheteurDto = new AcheteurDto() ;
		
		
	}

}