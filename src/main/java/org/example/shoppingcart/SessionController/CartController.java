package org.example.shoppingcart.SessionController;

import jakarta.servlet.http.HttpSession;
import org.example.shoppingcart.Model.Cart;
import org.example.shoppingcart.Model.CartItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    private Cart cart;


   private boolean isLoggedIn(HttpSession session) {
       return session.getAttribute("user") != null;
   }

   // metode til at hente indkøbskurven fra sessionen, eller oprette en ny hvis den ikke findes
   private Cart getOrCreateCart(HttpSession session) {
       Cart cart = (Cart) session.getAttribute("cart");

       if (cart == null) {
           cart = new Cart();
           session.setAttribute("cart", cart);
           session.setMaxInactiveInterval(30); //sæt sessionen til 30 sekunder
       }
       return cart;
   }

    //showCart-metoden gemmer indkøbskurven i Model, så den kan sendes direkte til visningen (cart.html).
    @GetMapping("/cart")
    public String showCart(Model model) {

        //Tilføj attributter
        model.addAttribute("items", this.cart.getItems());
        model.addAttribute("total", this.cart.getTotal());

        return "cart"; //// Returner "cart.html", der viser UI
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam String name,
                            @RequestParam double price,
                            @RequestParam int quantity,
                            HttpSession session){

       Cart cart = getOrCreateCart(session);

        // Opret et nyt og tilføj det til indkøbskurven
        CartItem newItem = new CartItem(name, price, quantity);
        cart.addItem(newItem);

        // Redirect til /cart for at vise opdateret indkøbskurv
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam int index, HttpSession session){
        Cart cart = getOrCreateCart(session);

        //Hvis indekset er gyldigt, fjern itemet fra indkøbskurven
        if (index >= 0 && index < cart.getItems().size()) {
            cart.removeItem(index);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/empty")
    public String emptyCart(HttpSession session){
        // Fjern 'cart' attributten fra session objektet
        session.removeAttribute("cart");
        // Redirect til /cart for at vise tømt kurv
        return "redirect:/cart";
    }

    

}
