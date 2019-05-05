package com.wherescape.vmsmicroservice.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.testcomapny.vmsmicroservice.VmsmicroserviceApp;
import com.testcomapny.vmsmicroservice.domain.Employee;
import com.testcomapny.vmsmicroservice.domain.Visitor;
import com.testcomapny.vmsmicroservice.repository.VisitorRepository;
import com.testcomapny.vmsmicroservice.service.VisitorService;
import com.testcomapny.vmsmicroservice.service.dto.VisitorDTO;
import com.testcomapny.vmsmicroservice.service.mapper.VisitorMapper;
import com.testcomapny.vmsmicroservice.web.rest.VisitorResource;
import com.testcomapny.vmsmicroservice.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.wherescape.vmsmicroservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VisitorResource REST controller.
 *
 * @see VisitorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VmsmicroserviceApp.class)
public class VisitorResourceIntTest {

    private static final String DEFAULT_FNAME = "AAAAAAAAAA";
    private static final String UPDATED_FNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LNAME = "AAAAAAAAAA";
    private static final String UPDATED_LNAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "871) 977 2759";
    private static final String UPDATED_MOBILE = "679) 9497563";

    private static final String DEFAULT_EMAIL = "6u@f\\dueW";
    private static final String UPDATED_EMAIL = "\\s@s\\XhH\\NOR";

    private static final String DEFAULT_COMPANYNAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANYNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CHECKIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHECKIN = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CHECKOUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHECKOUT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVisitorMockMvc;

    private Visitor visitor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VisitorResource visitorResource = new VisitorResource(visitorService);
        this.restVisitorMockMvc = MockMvcBuilders.standaloneSetup(visitorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visitor createEntity(EntityManager em) {
        Visitor visitor = new Visitor()
            .fname(DEFAULT_FNAME)
            .lname(DEFAULT_LNAME)
            .mobile(DEFAULT_MOBILE)
            .email(DEFAULT_EMAIL)
            .companyname(DEFAULT_COMPANYNAME)
            .checkin(DEFAULT_CHECKIN)
            .checkout(DEFAULT_CHECKOUT);
        // Add required entity
        Employee employee = EmployeeResourceIntTest.createEntity(em);
        em.persist(employee);
        em.flush();
        visitor.setEmployee(employee);
        return visitor;
    }

    @Before
    public void initTest() {
        visitor = createEntity(em);
    }

    @Test
    @Transactional
    public void createVisitor() throws Exception {
        int databaseSizeBeforeCreate = visitorRepository.findAll().size();

        // Create the Visitor
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);
        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isCreated());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeCreate + 1);
        Visitor testVisitor = visitorList.get(visitorList.size() - 1);
        assertThat(testVisitor.getFname()).isEqualTo(DEFAULT_FNAME);
        assertThat(testVisitor.getLname()).isEqualTo(DEFAULT_LNAME);
        assertThat(testVisitor.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testVisitor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVisitor.getCompanyname()).isEqualTo(DEFAULT_COMPANYNAME);
        assertThat(testVisitor.getCheckin()).isEqualTo(DEFAULT_CHECKIN);
        assertThat(testVisitor.getCheckout()).isEqualTo(DEFAULT_CHECKOUT);
    }

    @Test
    @Transactional
    public void createVisitorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visitorRepository.findAll().size();

        // Create the Visitor with an existing ID
        visitor.setId(1L);
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setFname(null);

        // Create the Visitor, which fails.
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);

        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setLname(null);

        // Create the Visitor, which fails.
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);

        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setMobile(null);

        // Create the Visitor, which fails.
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);

        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setEmail(null);

        // Create the Visitor, which fails.
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);

        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCheckinIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setCheckin(null);

        // Create the Visitor, which fails.
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);

        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCheckoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setCheckout(null);

        // Create the Visitor, which fails.
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);

        restVisitorMockMvc.perform(post("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVisitors() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList
        restVisitorMockMvc.perform(get("/api/visitors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME.toString())))
            .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].companyname").value(hasItem(DEFAULT_COMPANYNAME.toString())))
            .andExpect(jsonPath("$.[*].checkin").value(hasItem(DEFAULT_CHECKIN.toString())))
            .andExpect(jsonPath("$.[*].checkout").value(hasItem(DEFAULT_CHECKOUT.toString())));
    }

    @Test
    @Transactional
    public void getVisitor() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get the visitor
        restVisitorMockMvc.perform(get("/api/visitors/{id}", visitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(visitor.getId().intValue()))
            .andExpect(jsonPath("$.fname").value(DEFAULT_FNAME.toString()))
            .andExpect(jsonPath("$.lname").value(DEFAULT_LNAME.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.companyname").value(DEFAULT_COMPANYNAME.toString()))
            .andExpect(jsonPath("$.checkin").value(DEFAULT_CHECKIN.toString()))
            .andExpect(jsonPath("$.checkout").value(DEFAULT_CHECKOUT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVisitor() throws Exception {
        // Get the visitor
        restVisitorMockMvc.perform(get("/api/visitors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVisitor() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();

        // Update the visitor
        Visitor updatedVisitor = visitorRepository.findOne(visitor.getId());
        // Disconnect from session so that the updates on updatedVisitor are not directly saved in db
        em.detach(updatedVisitor);
        updatedVisitor
            .fname(UPDATED_FNAME)
            .lname(UPDATED_LNAME)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .companyname(UPDATED_COMPANYNAME)
            .checkin(UPDATED_CHECKIN)
            .checkout(UPDATED_CHECKOUT);
        VisitorDTO visitorDTO = visitorMapper.toDto(updatedVisitor);

        restVisitorMockMvc.perform(put("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isOk());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
        Visitor testVisitor = visitorList.get(visitorList.size() - 1);
        assertThat(testVisitor.getFname()).isEqualTo(UPDATED_FNAME);
        assertThat(testVisitor.getLname()).isEqualTo(UPDATED_LNAME);
        assertThat(testVisitor.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testVisitor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVisitor.getCompanyname()).isEqualTo(UPDATED_COMPANYNAME);
        assertThat(testVisitor.getCheckin()).isEqualTo(UPDATED_CHECKIN);
        assertThat(testVisitor.getCheckout()).isEqualTo(UPDATED_CHECKOUT);
    }

    @Test
    @Transactional
    public void updateNonExistingVisitor() throws Exception {
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();

        // Create the Visitor
        VisitorDTO visitorDTO = visitorMapper.toDto(visitor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVisitorMockMvc.perform(put("/api/visitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitorDTO)))
            .andExpect(status().isCreated());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVisitor() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);
        int databaseSizeBeforeDelete = visitorRepository.findAll().size();

        // Get the visitor
        restVisitorMockMvc.perform(delete("/api/visitors/{id}", visitor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visitor.class);
        Visitor visitor1 = new Visitor();
        visitor1.setId(1L);
        Visitor visitor2 = new Visitor();
        visitor2.setId(visitor1.getId());
        assertThat(visitor1).isEqualTo(visitor2);
        visitor2.setId(2L);
        assertThat(visitor1).isNotEqualTo(visitor2);
        visitor1.setId(null);
        assertThat(visitor1).isNotEqualTo(visitor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisitorDTO.class);
        VisitorDTO visitorDTO1 = new VisitorDTO();
        visitorDTO1.setId(1L);
        VisitorDTO visitorDTO2 = new VisitorDTO();
        assertThat(visitorDTO1).isNotEqualTo(visitorDTO2);
        visitorDTO2.setId(visitorDTO1.getId());
        assertThat(visitorDTO1).isEqualTo(visitorDTO2);
        visitorDTO2.setId(2L);
        assertThat(visitorDTO1).isNotEqualTo(visitorDTO2);
        visitorDTO1.setId(null);
        assertThat(visitorDTO1).isNotEqualTo(visitorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(visitorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(visitorMapper.fromId(null)).isNull();
    }
}
