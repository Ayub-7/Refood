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
    private WishlistItem wishlistItem3;
    private List<WishlistItem> wishlistItemListUser;
    private List<WishlistItem> wishlistItemsBusiness;
    private List<WishlistItem> wishlistItemsAll;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        wishlistItemRepository.deleteAll();
        wishlistItemRepository.flush();
        wishlistItemListUser = new ArrayList<>();
        wishlistItemsBusiness = new ArrayList<>();
        wishlistItemsAll = new ArrayList<>();
        assertThat(wishlistItemRepository).isNotNull();
        wishlistItem1 = new WishlistItem(Long.valueOf(1), Long.valueOf(2));
        wishlistItem2 = new WishlistItem(Long.valueOf(1), Long.valueOf(1));
        wishlistItem3 = new WishlistItem(Long.valueOf(2), Long.valueOf(1));
        wishlistItemRepository.save(wishlistItem1);
        wishlistItemRepository.save(wishlistItem2);
        wishlistItemRepository.save(wishlistItem3);
        wishlistItemListUser.add(wishlistItem1);
        wishlistItemListUser.add(wishlistItem2);
        wishlistItemsBusiness.add(wishlistItem2);
        wishlistItemsBusiness.add(wishlistItem3);
        wishlistItemsAll.add(wishlistItem1);
        wishlistItemsAll.add(wishlistItem2);
        wishlistItemsAll.add(wishlistItem3);
    }

    @Test
    void findWishListItemsByUserIdSuccessful() {
        assertThat(wishlistItemRepository.findWishlistItemsByUserId(Long.valueOf(1))).isEqualTo((wishlistItemListUser));
    }

    @Test
    void findWishListItemsByUserIdItemIsNull() {
        List<WishlistItem> notFound = wishlistItemRepository.findWishlistItemsByUserId(3);
        assertThat(notFound.size()).isEqualTo(0);
    }

    @Test
    void findSingleWishlistItemByIdSuccessful() {
        WishlistItem found = wishlistItemRepository.findWishlistItemById(4);
        assertThat(wishlistItem1).isEqualTo(found);
    }

    @Test
    void findNonExistentWishlistItemIsNull() {
        WishlistItem notFound = wishlistItemRepository.findWishlistItemById(10);
        assertThat(notFound).isNull();
    }

    @Test
    void findAllWishListItemsSuccessful() {
        assertThat(wishlistItemRepository.findAll()).isEqualTo((wishlistItemsAll));
    }

    @Test
    void findWishListItemsByBusinessIdSuccessful() {
        assertThat(wishlistItemRepository.findWishlistItemByBusinessId(Long.valueOf(1))).isEqualTo((wishlistItemsBusiness));
    }

    @Test
    void findWishListItemsByBusinessIdItemIsNull() {
        List<WishlistItem> notFound = wishlistItemRepository.findWishlistItemByBusinessId(10);
        assertThat(notFound.size()).isEqualTo(0);
    }


}
