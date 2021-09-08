package org.seng302.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.TestApplication;
import org.seng302.models.Address;
import org.seng302.models.User;
import org.seng302.models.WishlistItem;
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

    private WishlistItem wishlistItem1;
    private WishlistItem wishlistItem2;
    private List<WishlistItem> wishlistItemList;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        wishlistItemRepository.deleteAll();
        wishlistItemRepository.flush();
        wishlistItemList = new ArrayList<>();
        assertThat(wishlistItemRepository).isNotNull();
        wishlistItem1 = new WishlistItem(Long.valueOf(1), Long.valueOf(2));
        wishlistItem2 = new WishlistItem(Long.valueOf(1), Long.valueOf(1));
        System.out.println(wishlistItem1);
        wishlistItemRepository.save(wishlistItem1);
        wishlistItemRepository.save(wishlistItem2);
        wishlistItemList.add(wishlistItem1);
        wishlistItemList.add(wishlistItem2);
    }

    @Test
    void findSingleWishlistItemByIdSuccessful() {
        WishlistItem found = wishlistItemRepository.findWishlistItemById(1);
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

    @Test
    void findWishListItemsByUserIdSuccessful() {
        assertThat(wishlistItemRepository.findWishlistItemsByUserId(Long.valueOf(1))).isEqualTo((wishlistItemList));
    }

    @Test
    void findWishListItemsByUserIdItemIsNull() {
        List<WishlistItem> notFound = wishlistItemRepository.findWishlistItemsByUserId(2);
        assertThat(notFound.size()).isEqualTo(0);
    }
}
