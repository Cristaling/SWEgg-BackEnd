package io.github.cristaling.swegg.backend.repositories;

import io.github.cristaling.swegg.backend.core.recommendations.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecommendRepository extends JpaRepository<Recommend, UUID> {
    Recommend getRecommendByRecommendedEmailAndRecommenderEmailAndReceiver(String recommended, String recommender, String receiver);

    List<Recommend> getRecommendsByRecommendedEmail(String recommended);
}
