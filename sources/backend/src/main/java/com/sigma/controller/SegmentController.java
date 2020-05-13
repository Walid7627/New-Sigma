package com.sigma.controller;

import com.sigma.dto.SegmentDto;
import com.sigma.model.ApiResponse;
import com.sigma.model.Fournisseur;
import com.sigma.model.Segment;
import com.sigma.repository.SegmentRepository;
import com.sigma.util.IterableToList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.servlet.http.HttpServletRequest;



@Controller
@RequestMapping("/api/segment")
public class SegmentController {
	
	
    //@RequestMapping("/list")
	@GetMapping("/list")
    @ResponseBody
    public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
        try {
            List<Segment> users = IterableToList.toList(segmentRepository.findAll());
            return objectMapper.writeValueAsString(users);
        } catch (Exception ex) {
          return objectMapper.writeValueAsString(
            new ApiResponse(HttpStatus.BAD_REQUEST,
                            "Unable to find users",
                            ex)
          );
        }
    }

    /**
     * POT /create  --> Create a new user and save it in the database.
     */
	@PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody SegmentDto segment) throws com.fasterxml.jackson.core.JsonProcessingException {
    	Segment s;
		if (segment == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Name cannot be empty")
					);
		}

          s = new Segment(segment.getLibelle(), segment.getCodeCPV(), segment.getCodeAPE());
          segmentRepository.save(s);
     

      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.OK,
                        "Team successfully created\nReceived input:\n" + objectMapper.writeValueAsString(segment))
      );
    }

    /**
     * GET /delete  --> Delete the user having the passed id.
     */
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
        try {
            Segment s = segmentRepository.findOne(id);
            segmentRepository.delete(s);
        } catch (Exception ex) {
          return objectMapper.writeValueAsString(
            new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Error when deleting the segment",
                          ex)
          );
        }

        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.OK,
                          "Segment successfully deleted")
        );
    }


    /**
     * GET /update  --> Update the email and the name for the user in the
     * database having the passed id.
     */
    @RequestMapping("/update")
    @ResponseBody
    public String updateSegment(@RequestBody SegmentDto segment) throws com.fasterxml.jackson.core.JsonProcessingException {
		Segment seg;
		try {
			seg = segmentRepository.findOne(segment.getId());

			seg.setLibelle(segment.getLibelle());
			seg.setCodeCPV(segment.getCodeCPV());
			seg.setCodeAPE(segment.getCodeAPE());

			segmentRepository.save(seg);
		}
		catch (Exception ex) {
			return "Error updating the segment: " + ex.toString();
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(seg))
				);

    }

    
    
    /*  private Segment mapSegmentDTOToSegment(SegmentDto segmentDto) {
        ModelMapper mapper = new ModelMapper();
        Segment segment = mapper.map(segmentDto, Segment.class);
        return segment;
    }*/

    // Private fields

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
}