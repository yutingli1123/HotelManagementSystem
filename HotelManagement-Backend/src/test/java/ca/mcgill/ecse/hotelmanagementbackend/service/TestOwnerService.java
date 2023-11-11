package ca.mcgill.ecse.hotelmanagementbackend.service;

import ca.mcgill.ecse.hotelmanagementbackend.entity.Owner;
import ca.mcgill.ecse.hotelmanagementbackend.repository.OwnerRepository;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

/**
 * Test for Owner Service class
 * @author Clara Jabbour
 */
@ExtendWith(MockitoExtension.class)
public class TestOwnerService {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testFindById() {
        // Define a sample owner
        Owner owner = new Owner();
        owner.setId(1L);

        // Mock the behavior of ownerRepository.findById()
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));

        // Define an ID that doesn't exist in the repository
        Long nonExistentId = 2L;

        // Mock the behavior of ownerRepository.findById() when the ID is not found
        when(ownerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Verify that the service returns the expected owner when found
        Owner foundOwner = ownerService.findById(1L);
        assertNotNull(foundOwner);
        assertEquals(owner.getId(), foundOwner.getId());

        // Verify that the service returns null when the ID is not found
        Owner nonExistentOwner = ownerService.findById(nonExistentId);
        assertNull(nonExistentOwner);
    }

    @Test
    public void testFindAll() {
        // Define a list of sample owners
        List<Owner> owners = List.of(new Owner(), new Owner());

        // Mock the behavior of ownerRepository.findAll()
        when(ownerRepository.findAll()).thenReturn(owners);

        // Call the service to get all owners
        List<Owner> foundOwners = ownerService.findAll();

        // Verify that the service returns the expected list of owners
        assertNotNull(foundOwners);
        assertEquals(2, foundOwners.size());
    }

    @Test
    public void testSave() {
        // Define a sample owner to be saved
        Owner owner = new Owner();
        owner.setId(1L);

        // Mock the behavior of ownerRepository.save() to return the owner
        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

        // Call the service to save the owner
        ownerService.save(owner);

        // Verify that the service calls ownerRepository.save with the correct argument
        verify(ownerRepository, times(1)).save(owner);
    }


    @Test
    public void testDeleteById() {
        // Mock the behavior of ownerRepository.deleteById()
        ownerService.deleteById(1L);

        // Verify that the service calls deleteById with the correct argument
        verify(ownerRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByIdWithInvalidInput() {
        // Define an invalid (negative) ID
        Long invalidId = -1L;

        // Call the service to find an owner with an invalid ID
        Owner foundOwner = ownerService.findById(invalidId);

        // Verify that the service returns null for an invalid input
        assertNull(foundOwner);
    }

    @Test
    public void testDeleteByIdWithInvalidInput() {
        // Define an invalid (negative) ID
        Long invalidId = -1L;

        // Mock the behavior of ownerRepository.deleteById() to throw EntityNotFoundException
        doThrow(EntityNotFoundException.class).when(ownerRepository).deleteById(invalidId);

        // Attempt to delete an owner with an invalid ID
        assertThrows(EntityNotFoundException.class, () -> ownerService.deleteById(invalidId));

        // Verify that the service calls deleteById with the correct argument
        verify(ownerRepository, times(1)).deleteById(invalidId);
    }


}



