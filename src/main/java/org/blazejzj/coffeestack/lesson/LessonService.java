package org.blazejzj.coffeestack.lesson;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LessonService {

    public List<String> getAllSlugs;
}
