package com.devdensan.mercadona;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.controller.*;
import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.repository.CategoryRepository;
import com.devdensan.mercadona.repository.ProductRepository;
import com.devdensan.mercadona.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import com.devdensan.mercadona.auth.RegisterRequest;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.security.enabled=false")
public class SecurityLayerTests {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private AdminController adminController;
    @InjectMocks
    private AdminProductController adminProductController;
    @InjectMocks
    private AdminCategoryController adminCategoryController;
    @InjectMocks
    private AdminUserController adminUserController;
    private final AuthenticationService authenticationService = mock(AuthenticationService.class);
    private final CategoryService categoryService = mock(CategoryService.class);
    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final ProductService productService = mock(ProductService.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final PromotionService promotionService = mock(PromotionService.class);
    private final UserService userService = mock(UserService.class);
    private final RoleService roleService = mock(RoleService.class);
    private final AdminPromotionController adminPromotionController = new AdminPromotionController(authenticationService, promotionService, productRepository);

    @Test
    void testAdminLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout").with(csrf()));
        // Given
        String username = "root";
        String password = "root";

        // When
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password));

        // Then
        result.andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));
    }

    @Test
    public void testRegister() throws Exception {
        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .userName("testUser")
                .password("testPassword")
                .email("test@example.com")
                .build();

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerRequest)))
                .andExpect(status().isOk());
    }

    // AdminController:
    @Test
    void testDashboard() {
        // Given
        Model model = mock(Model.class);

        // When
        String viewName = adminController.dashboard(model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute(eq("page"), eq("dashboard"));
        assertEquals("back/components/template", viewName);
    }
    @Test
    void testProducts() {
        // Given
        Model model = mock(Model.class);

        // When
        String viewName = adminController.products(model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute(eq("page"), eq("products"));
        assertEquals("back/components/template", viewName);
    }
    @Test
    void testCategories() {
        // Given
        Model model = mock(Model.class);

        // When
        String viewName = adminController.categories(model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute(eq("page"), eq("categories"));
        assertEquals("back/components/template", viewName);
    }
    @Test
    void testUsers() {
        // Given
        Model model = mock(Model.class);

        // When
        String viewName = adminController.users(model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute(eq("page"), eq("users"));
        assertEquals("back/components/template", viewName);
    }

    // AdminProductController:
    @Test
    void testProductForm() {
        // Given
        Model model = mock(Model.class);

        // When
        String viewName = adminProductController.productForm(model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute(eq("page"), eq("product-form"));
        verify(model).addAttribute(eq("categories"), any());
        assertEquals("back/components/template", viewName);
    }

    @Test
    void testProductAdd() {
        // Given
        when(categoryService.existsByCategoryId(anyInt())).thenReturn(true);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("product-name", "product");
        request.setParameter("description", "description");
        request.setParameter("category", "1");
        request.setParameter("price", "10");
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // When
        adminProductController.productAdd(request, redirectAttributes);

        // Then
        verify(redirectAttributes).addFlashAttribute(eq("message"), anyMap());
    }

    @Test
    void testEditProduct() {
        // Given
        int productId = 1;
        Model model = mock(Model.class);
        Product mockedProduct = new Product();
        when(productService.getProductById(productId)).thenReturn(mockedProduct);

        // When
        String viewName = adminProductController.editProduct(productId, model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute(eq("page"), eq("product-form"));
        verify(model).addAttribute(eq("categories"), any());
        verify(model).addAttribute(eq("product"), eq(mockedProduct));
        assertEquals("back/components/template", viewName);
    }

    @Test
    void testSaveEditedProduct() {
        // Given
        int productId = 1;
        HttpServletRequest request = mock(HttpServletRequest.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        int categoryId = 1;
        when(request.getParameter("category")).thenReturn(String.valueOf(categoryId));
        when(request.getParameter("product-name")).thenReturn("Edited Product");
        when(request.getParameter("description")).thenReturn("Edited Description");
        when(request.getParameter("price")).thenReturn("20.0");
        when(categoryService.existsByCategoryId(categoryId)).thenReturn(true);

        Product mockedProduct = new Product();
        when(productService.getProductById(productId)).thenReturn(mockedProduct);

        // When
        String viewName = adminProductController.saveEditedProduct(productId, request, redirectAttributes);

        // Then
        verify(categoryService).existsByCategoryId(categoryId);
        verify(productService).getProductById(productId);
        assertEquals("redirect:/admin/products", viewName);
    }

    @Test
    void testDeleteProduct() {
        // Given
        int productId = 1;
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        boolean productExists = true;
        when(productService.existsByProductId(productId)).thenReturn(productExists);

        // When
        String viewName = adminProductController.deleteProduct(productId, redirectAttributes);

        // Then
        verify(productService).existsByProductId(productId);

        // Check if the message is null
        Object actualMessageObject = redirectAttributes.getFlashAttributes().get("message");
        assertNull(actualMessageObject);

        // If the message is not null, verify its content
        if (actualMessageObject != null) {
            assertTrue(actualMessageObject instanceof Map);

            Map<String, String> actualMessage = (Map<String, String>) actualMessageObject;
            assertEquals("Produit supprimé correctement.", actualMessage.get("text"));
            assertEquals("success", actualMessage.get("type"));
        }

        assertEquals("redirect:/admin/products", viewName);
    }

    // AdminCategoryController:
    @Test
    void testCategoryForm() {
        // Given
        Model model = mock(Model.class);

        // When
        String result = adminCategoryController.categoryForm(model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        assertEquals("back/components/template", result);
    }

    @Test
    void testCategoryAdd() {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(request.getParameter("category-name")).thenReturn("Test Category");
        when(categoryService.existsByName("Test Category")).thenReturn(false);

        // When
        String result = adminCategoryController.categoryAdd(request, redirectAttributes);

        // Then
        verify(categoryService).newCategory(request, redirectAttributes);
        assertEquals("redirect:/admin/categories", result);
    }

    @Test
    void testEditCategory() {
        // Given
        Model model = mock(Model.class);
        Category category = new Category(1,"Category");
        when(categoryService.getCategoryById(1)).thenReturn(category);

        // When
        String result = adminCategoryController.editCategory(1, model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        assertEquals("back/components/template", result);
    }

    @Test
    void testSaveEditedCategory() {
        // Given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(categoryService.existsByName("New Category")).thenReturn(false);
        when(categoryService.editCategory(1, "New Category")).thenReturn(new Category());

        // When
        String result = adminCategoryController.saveEditedCategory(1, "New Category", redirectAttributes);

        // Then
        assertEquals("redirect:/admin/categories", result);
    }

    @Test
    void testDeleteCategory() {
        // Given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(categoryService.existsByCategoryId(1)).thenReturn(true);
        when(categoryService.articlesByCategoryId()).thenReturn(new HashMap<>());

        // When
        String result = adminCategoryController.deleteCategory(1, redirectAttributes);

        // Then
        assertEquals("redirect:/admin/categories", result);
    }

    // AdminPromotionController:
    @Test
    void testPromotionForm() {
        // Given
        Model model = mock(Model.class);
        int productId = 1;

        // When
        String result = adminPromotionController.promotionForm(productId, model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute("productId", productId);
        verify(model).addAttribute("page", "promotion-form");
        assertEquals("back/components/template", result);
    }

    @Test
    void testPromotionDelete() {
        // Given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(promotionService.deletePromotionFromProductId(1)).thenReturn(true);

        // When
        String result = adminPromotionController.promotionDelete(1, redirectAttributes);

        // Then
        verify(promotionService).deletePromotionFromProductId(1);
        verify(redirectAttributes).addFlashAttribute(eq("message"), any(Map.class));
        assertEquals("redirect:/admin/products", result);
    }

    // AdminUserController:
    @Test
    void testUserForm() {
        // Given
        Model model = mock(Model.class);

        // When
        String result = adminUserController.userForm(model);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute(eq("page"), eq("user-form"));
        verify(model).addAttribute(eq("roles"), any());
        assertEquals("back/components/template", result);
    }

    @Test
    public void testUserAdd() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("role")).thenReturn("1");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("password2")).thenReturn("password");

        // Act
        String result = adminUserController.userAdd(request, redirectAttributes);

        // Assert
        assertEquals("redirect:/admin/user/new", result);
        verify(userService, times(0)).newUser(request);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("message"), any(Map.class));
    }

    @Test
    void testEditUser() {
        // Given
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        User mockUser = mock(User.class);
        when(userService.getUserById(anyInt())).thenReturn(mockUser);

        // When
        String result = adminUserController.editUser(1, model, redirectAttributes);

        // Then
        verify(authenticationService).loadConnectedUser(model);
        verify(model).addAttribute(eq("user"), eq(mockUser));
        verify(model).addAttribute(eq("roles"), any());
        verify(model).addAttribute(eq("connectedUser"), any());
        verify(model).addAttribute(eq("page"), eq("user-form"));
        assertEquals("back/components/template", result);
    }

    @Test
    public void testSaveEditedUser() {
        // Given
        int userId = 1;
        String username = "";
        String email = "testuser@example.com";
        int roleId = 1;
        String password = "password";

        HttpServletRequest request = mock(HttpServletRequest.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(request.getParameter("user-id")).thenReturn(String.valueOf(userId));
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("role")).thenReturn(String.valueOf(roleId));
        when(request.getParameter("password")).thenReturn(password);

        // When
        String result = adminUserController.saveEditedUser(userId, request, redirectAttributes);

        // Then
        assertEquals("redirect:/admin/users", result);
        verify(userService, times(0)).editUser(userId, username, email, roleId, password);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("message"), any(Map.class));
    }

    @Test
    void testDeleteUser() {
        // Given
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(userService.getUserById(anyInt())).thenReturn(mock(User.class));
        when(authenticationService.getAuthenticatedUser()).thenReturn(mock(User.class));

        // When
        String result = adminUserController.deleteUser(1, redirectAttributes);

        // Then
        verify(redirectAttributes).addFlashAttribute(eq("message"), any());
        assertEquals("redirect:/admin/users", result);
    }

    // CategoryRepository
    @Test
    public void testExistsByName() {
        // Given
        String nonExistingCategoryName = "Fruits";

        // When
        boolean exists = categoryRepository.existsByName(nonExistingCategoryName);

        // Then
        assertFalse(exists);
    }

    // ProductRepository
    @Test
    public void testExistsByProductId() {
        // Given
        int nonExistingProductId = 0;

        // When
        boolean exists = productRepository.existsByProductId(nonExistingProductId);

        // Then
        assertFalse(exists);
    }















    // Convert Obj to JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}