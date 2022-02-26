package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void  messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        Arrays.stream(messageCodes).forEach(text -> System.out.println("messageCode = " + text));

        // MessageCodesResolver가 밑에와 같이 만들어준다.
        // new ObjectError("item", new String[]{"required.item", "required"});

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        Arrays.stream(messageCodes).forEach(text -> System.out.println("messageCode = " + text));
        assertThat(messageCodes).containsExactly(
            "required.item.itemName",
            "required.itemName",
            "required.java.lang.String",
            "required"
        );

//        bindingResult.rejectValue("itemName", "required");
//        bindingResult가 내부적으로 MessageResolver를 사용해서 다음과 같이 만든다.
//        new FieldError("item", "itemName", null, false, messageCodes, null, null);
    }
}
