package com.todoAPI.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoAPI.todo.models.Task;
import com.todoAPI.todo.controllers.TaskController;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TodoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

	@Test
    public void testCreateTask_Success() throws Exception {
        Task task = new Task();
        task.setName("Tarea de ejemplo");
        task.setPriority(1);
		task.setPriorityName("Low");
        task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/todo/task")
                .content(asJsonString(task))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // Verificar la respuesta
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(asJsonString(task), result.getResponse().getContentAsString());
    }

	@Test
	public void testCreateTask_InvalidName() throws Exception {
		Task task = new Task();
		task.setName("");
		task.setPriority(1);
		task.setPriorityName("Low");
		task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void testCreateTask_InvalidPriority() throws Exception {
		Task task = new Task();
		task.setName("Tarea de ejemplo");
		task.setPriority(0);
		task.setPriorityName("Low");
		task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void testCreateTask_InvalidDueDate() throws Exception {
		Task task = new Task();
		task.setName("Tarea de ejemplo");
		task.setPriority(1);
		task.setPriorityName("Low");
		task.setDueDate(LocalDateTime.now().minusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
	}

	@Test
	public void testGetAllTasks_Success() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/api/v1/todo/task"))
				.andReturn();

		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	@Test
	public void testModifyTask_Success() throws Exception {
		Task task = new Task();
		task.setName("Tarea de ejemplo");
		task.setPriority(1);
		task.setPriorityName("Low");
		task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		
		task.setName("Tarea modificada");
		task.setPriority(2);
		task.setPriorityName("Medium");
		task.setDueDate(LocalDateTime.now().plusDays(2));
		
		MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
				.put("/api/v1/todo/task/" + task.getId())
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertEquals(HttpStatus.OK.value(), updateResult.getResponse().getStatus());
		assertEquals(asJsonString(task), updateResult.getResponse().getContentAsString());
	}

	@Test
	public void testModifyTask_InvalidId() throws Exception {
		Task task = new Task();
		task.setName("Tarea de ejemplo");
		task.setPriority(1);
		task.setPriorityName("Low");
		task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		task.setName("Tarea modificada");
		task.setPriority(2);
		task.setPriorityName("Medium");
		task.setDueDate(LocalDateTime.now().plusDays(2));

		MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
				.put("/api/v1/todo/task/" + (task.getId() + 1))
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertEquals(HttpStatus.NOT_FOUND.value(), updateResult.getResponse().getStatus());
	}

	@Test
	public void testModifyTask_InvalidName() throws Exception {
		Task task = new Task();
		task.setName("Tarea de ejemplo");
		task.setPriority(1);
		task.setPriorityName("Low");
		task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		task.setName("");
		task.setPriority(2);
		task.setPriorityName("Medium");
		task.setDueDate(LocalDateTime.now().plusDays(2));

		MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
				.put("/api/v1/todo/task/" + task.getId())
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
			
		assertEquals(HttpStatus.BAD_REQUEST.value(), updateResult.getResponse().getStatus());
	}

	@Test
	public void testModifyTask_InvalidPriority() throws Exception {
		Task task = new Task();
		task.setName("Tarea de ejemplo");
		task.setPriority(1);
		task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		task.setName("Tarea modificada");
		task.setPriority(0);
		task.setPriorityName("");
		task.setDueDate(LocalDateTime.now().plusDays(2));

		MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
				.put("/api/v1/todo/task/" + task.getId())
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), updateResult.getResponse().getStatus());
	}

	@Test
	public void testModifyTask_InvalidDueDate() throws Exception {
		Task task = new Task();
		task.setName("Tarea de ejemplo");
		task.setPriority(1);
		task.setPriorityName("Low");
		task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		task.setName("Tarea modificada");
		task.setPriority(2);
		task.setPriorityName("Medium");
		task.setDueDate(LocalDateTime.now().minusDays(2));

		MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders
				.put("/api/v1/todo/task/" + task.getId())
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), updateResult.getResponse().getStatus());
	}

	@Test
	public void testDeleteTask_Success() throws Exception {
		Task task = new Task();
		task.setName("Tarea de ejemplo");
		task.setPriority(1);
		task.setPriorityName("Low");
		task.setDueDate(LocalDateTime.now().plusDays(1));

		Task.id -= 1;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/todo/task")
				.content(asJsonString(task))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/v1/todo/task/" + task.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		assertEquals(HttpStatus.OK.value(), deleteResult.getResponse().getStatus());
	}

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }   
    }
}
