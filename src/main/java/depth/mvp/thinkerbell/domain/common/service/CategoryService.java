package depth.mvp.thinkerbell.domain.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class CategoryService {

    private Map<String, String> categoryMap1;
    private Map<String, String> categoryMap2;

    public CategoryService() {
        convertJsonKeysEnglishToKorea();
        convertJsonKeysSnakeToPascal();
    }

    private void convertJsonKeysEnglishToKorea() {
        ObjectMapper objectMapper = new ObjectMapper();

        try(InputStream inputStream = new ClassPathResource("Categories.json").getInputStream()) {
            categoryMap1 = objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("카테고리 메핑 파일을 로드하는 동안 오류가 발생했습니다.",e);
        }
    }

    private void convertJsonKeysSnakeToPascal() {
        ObjectMapper objectMapper = new ObjectMapper();

        try(InputStream inputStream = new ClassPathResource("Category.json").getInputStream()) {
            categoryMap2 = objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("카테고리 메핑 파일을 로드하는 동안 오류가 발생했습니다.",e);
        }
    }

    public String convertPascalToSnake(String pascalStr) {
        StringBuilder snakeStr = new StringBuilder();

        for (int i = 0; i < pascalStr.length(); i++) {
            char currentChar = pascalStr.charAt(i);
            // 대문자인 경우
            if (Character.isUpperCase(currentChar)) {
                // 첫 번째 문자가 아니면 '_'를 추가
                if (snakeStr.length() > 0) {
                    snakeStr.append('_');
                }
                // 대문자를 소문자로 변환하여 추가
                snakeStr.append(Character.toLowerCase(currentChar));
            } else {
                // 소문자 문자는 그대로 추가
                snakeStr.append(currentChar);
            }
        }

        return snakeStr.toString();
    }

    public String convertEnglishToKorea(String category) {
        return categoryMap1.getOrDefault(category, category);
    }

    public String convertSnakeToPascal(String category) {
        return categoryMap2.getOrDefault(category, category);
    }
}
