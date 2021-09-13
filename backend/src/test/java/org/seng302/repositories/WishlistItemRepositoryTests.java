package org.seng302.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.TestApplication;
import org.seng302.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests of the wishlistItem repository.
 */
@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
class WishlistItemRepositoryTests {
    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private BusinessRepository businessRepository;

    private WishlistItem wishlistItem1;
    private WishlistItem wishlistItem2;
    private List<WishlistItem> wishlistItemList;
    private Business business1;
    private Business business2;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        wishlistItemRepository.deleteAll();
        wishlistItemRepository.flush();
        Address addr = new Address(null, null, null, null, null, "Australia", "12345");
        business1 = new Business("testBusiness", "test description", addr, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES);
        business2 = new Business("testBusiness", "test description", addr, BusinessType.RETAIL_TRADE);
        businessRepository.save(business1);
        businessRepository.save(business2);
        wishlistItemList = new ArrayList<>();
        assertThat(wishlistItemRepository).isNotNull();
        wishlistItem1 = new WishlistItem(Long.valueOf(1), business1);
        wishlistItem2 = new WishlistItem(Long.valueOf(1), business2);
        System.out.println(wishlistItem1);
        wishlistItemRepository.save(wishlistItem1);
        wishlistItemRepository.save(wishlistItem2);
        wishlistItemList.add(wishlistItem1);
        wishlistItemList.add(wishlistItem2);
    }

    @Test
    void findWishListItemsByUserIdSuccessful() {
        System.out.println(wishlistItemRepository.findWishlistItemsByUserId(Long.valueOf(1)));
        assertThat(wishlistItemRepository.findWishlistItemsByUserId(Long.valueOf(1))).isEqualTo((wishlistItemList));
    }

    @Test
    void findWishListItemsByUserIdItemIsNull() {
        List<WishlistItem> notFound = wishlistItemRepository.findWishlistItemsByUserId(2);
        assertThat(notFound.size()).isZero();
    }

    @Test
    void findSingleWishlistItemByIdSuccessful() {
        WishlistItem found = wishlistItemRepository.findWishlistItemById(3);
        assertThat(wishlistItem1).isEqualTo(found);
    }

    @Test
    void findNonExistentWishlistItemIsNull() {
        WishlistItem notFound = wishlistItemRepository.findWishlistItemById(10);
        assertThat(notFound).isNull();
    }

    @Test
    void findAllWishListItemsSuccessful() {
        assertThat(wishlistItemRepository.findAll()).isEqualTo((wishlistItemList));
    }


}
