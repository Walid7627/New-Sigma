package visiteurTests;

import org.junit.Before;
import org.junit.Test;

import com.sigma.model.Address;
import com.sigma.model.Entite;
import com.sigma.model.Visiteur;

import java.util.Date;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class VisiteurTest {

    private Visiteur visiteur;

    @Before
    public void setUp() throws Exception {
        visiteur = new Visiteur();
    }

    @Test
    public void TestConstructeurVisiteurAvecParametre() throws Exception {
        //Given
        Visiteur visiteur; 
        String nom;
        String prenom;
        String mail;
        Address adresse;
        String telephone;
        String mobile;
		String password;
		Date dateEnregistrement;
		Entite entite;
        //When
		nom="albert";
		prenom="prnm";
		mail="mimo@gmail.com";
		adresse=new Address("107", "pascal", "76000","Rouen","France");
		telephone="07777777";
	    mobile="07777777";
		password="mmmm";
		dateEnregistrement=new Date();
		//entite;
		entite= new Entite();
        visiteur=new Visiteur( nom, prenom,  mail,  adresse,  telephone,  mobile,
			 password,  dateEnregistrement,  entite);
        
        //Then
        assertThat(visiteur.getNom()).isEqualTo(nom);
        assertThat(visiteur.getPrenom()).isEqualTo(prenom);
        assertThat(visiteur.getMail()).isEqualTo(mail);
        assertThat(visiteur.getAdresse()).isEqualTo(adresse);
        assertThat(visiteur.getTelephone()).isEqualTo(telephone);
        assertThat(visiteur.getMobile()).isEqualTo(mobile);
        assertThat(visiteur.getPassword()).isEqualTo(password);
        assertThat(visiteur.getDateEnregistrement()).isEqualTo(dateEnregistrement);
        assertThat(visiteur.getEntite()).isEqualTo(entite);
        //assertThat()
    }
    
    @Test
    public void TestSetEntite() throws Exception {
        //Given
        Visiteur visiteur; 
        String nom;
        String prenom;
        String mail;
        Address adresse;
        String telephone;
        String mobile;
		String password;
		Date dateEnregistrement;
		Entite entite;
        //When
		nom="albert";
		prenom="prnm";
		mail="mimo@gmail.com";
		adresse=new Address("107", "pascal", "76000","Rouen","France");
		telephone="07777777";
	    mobile="07777777";
		password="mmmm";
		dateEnregistrement=new Date();
		//entite;
		entite= new Entite();
        visiteur=new Visiteur( nom, prenom,  mail,  adresse,  telephone,  mobile,
			 password,  dateEnregistrement,  null);
        visiteur.setEntite(entite);
        
        //Then
        assertThat(visiteur.getEntite()).isEqualTo(entite);
    }

  

}
