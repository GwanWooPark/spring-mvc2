package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.domain.item.dto.ItemSaveDto;
import hello.itemservice.domain.item.dto.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationControllerV4 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveDto itemSaveDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (itemSaveDto.getPrice() != null && itemSaveDto.getQuantity() != null) {
            int resultPrice = itemSaveDto.getPrice() * itemSaveDto.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            // Spring이 modelAttribute에 자동으로 담아준다.
            log.info("errors = {}", bindingResult);

            return "validation/v4/addForm";
        }

        Item item = new Item();
        item.setItemName(itemSaveDto.getItemName());
        item.setPrice(itemSaveDto.getPrice());
        item.setQuantity(itemSaveDto.getQuantity());

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateDto itemUpdateDto, BindingResult bindingResult) {

        if (itemUpdateDto.getPrice() != null && itemUpdateDto.getQuantity() != null) {
            int resultPrice = itemUpdateDto.getPrice() * itemUpdateDto.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            // Spring이 modelAttribute에 자동으로 담아준다.
            log.info("errors = {}", bindingResult);

            return "validation/v4/editForm";
        }

        Item item = new Item();
        item.setItemName(itemUpdateDto.getItemName());
        item.setPrice(itemUpdateDto.getPrice());
        item.setQuantity(itemUpdateDto.getQuantity());
        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }
}
