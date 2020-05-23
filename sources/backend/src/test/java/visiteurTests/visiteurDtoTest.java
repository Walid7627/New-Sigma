package visiteurTests;

import org.junit.Before;
import org.junit.Test;

import com.sigma.dto.UtilisateurDto;
import com.sigma.dto.VisiteurDto;
import com.sigma.model.Entite;

import static org.assertj.core.api.Assertions.assertThat;

public class visiteurDtoTest {

    

    @Test
    public void TestGetAndSetEntite() throws Exception {
        //Given
    	VisiteurDto visiteurDto; 
        
		Entite entite;
        //When
		entite= new Entite();
		visiteurDto=  new VisiteurDto();
		visiteurDto.setEntite(entite);
        
        //Then
       assertThat(visiteurDto.getEntite()).isEqualTo(entite);
        
    }
    
  

}
