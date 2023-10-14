package ru.skypro.homework.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exeptions.NotFoundException;
import ru.skypro.homework.service.entities.AdEntity;
import ru.skypro.homework.service.entities.CommentEntity;
import ru.skypro.homework.service.repositories.AdRepository;
import ru.skypro.homework.service.repositories.CommentRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MySecurityService {

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;

    public MySecurityService(AdRepository adRepository, CommentRepository commentRepository) {
        this.adRepository = adRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Проверяем, что пользователь удаляющий коментарий являеятся его создателем или АДМИНОМ
     */
    public boolean canDeleteComment(int commentId, int adId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CommentEntity commentEntity = checkForAdAndComment(adId, commentId);
        String name = commentEntity.getUser().getEmail();

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        return authentication.getName().equals(name);
    }

    private CommentEntity checkForAdAndComment(int adId, int commentId) {
        adRepository.findById(adId).orElseThrow(() -> new NotFoundException(
                String.format("Объявление с индексом \"%s\" не найдено.", adId)
        ));

        return commentRepository.findCommentByCommentIdAndAdId(adId, commentId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Комментарий с индексом \"%s\" не найден для объявления с индексом \"%s\".",
                        commentId, adId))
                );
    }
}
