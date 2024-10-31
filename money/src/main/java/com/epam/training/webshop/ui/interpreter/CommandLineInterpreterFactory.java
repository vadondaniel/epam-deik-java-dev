package com.epam.training.webshop.ui.interpreter;

import com.epam.training.webshop.ui.command.AbstractCommand;
import com.epam.training.webshop.ui.command.UserCartAddProductCommand;
import com.epam.training.webshop.ui.command.UserCartCheckoutCommand;
import com.epam.training.webshop.ui.command.UserCartClearCommand;
import com.epam.training.webshop.ui.command.UserCartListCommand;
import com.epam.training.webshop.ui.command.UserCartRemoveProductCommand;
import com.epam.training.webshop.ui.command.UserProductListCommand;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;
import com.epam.training.webshop.core.cart.grosspricecalculator.impl.GrossPriceCalculatorImpl;
import com.epam.training.webshop.core.cart.grosspricecalculator.impl.HUGrossPriceCalculatorDecorator;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.ProductServiceImpl;
import java.util.List;

public class CommandLineInterpreterFactory {

  public static CommandLineInterpreter createInterpreter() {
    GrossPriceCalculator grossPriceCalculator = new HUGrossPriceCalculatorDecorator(
        new GrossPriceCalculatorImpl());

    ProductService productService = new ProductServiceImpl();

    List<AbstractCommand> abstractCommands = List.of(
        new UserCartListCommand(),
        new UserCartCheckoutCommand(grossPriceCalculator),
        new UserCartAddProductCommand(productService),
        new UserCartRemoveProductCommand(productService),
        new UserCartClearCommand(),
        new UserProductListCommand(productService)
    );
    return new CommandLineInterpreter(abstractCommands);
  }

}
