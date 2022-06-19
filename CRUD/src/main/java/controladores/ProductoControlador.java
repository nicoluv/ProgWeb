package controladores;

import io.javalin.Javalin;
import servicios.ProductoServicio;
import java.util.*;
import modelos.Producto;
import modelos.Usuario;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ProductoControlador {
    private Javalin app;
    private ProductoServicio prodServicio = ProductoServicio.getInstancia();

    private ArrayList<Producto> productos;

    public ProductoControlador(Javalin app) {
        productos = prodServicio.getProductos();
        this.app = app;
    }


    public void aplicarRutas(){
        app.get("/", ctx -> {
            ctx.redirect("comprar");
        });

        app.routes(() -> {
            path("/CRUD", () ->{
                before("/",ctx -> {
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if(usuario == null){
                        ctx.redirect("/login/"+0);
                    }
                });

                get("/", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant", prodServicio.getProductosCarrito().size());
                    modelo.put("titulo", "CRUD - Productos");
                    modelo.put("productos", productos);

                    if(ctx.sessionAttribute("usuario") != null){
                        modelo.put("sesion", true);
                    }else{
                        modelo.put("sesion", false);
                    }

                    ctx.render("/publico/CRUD-Productos.html",modelo);
                });
            });
        });


        app.get("/nuevoProducto", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant", prodServicio.getProductosCarrito().size());
            modelo.put("titulo", "Crear producto");
            modelo.put("action", "/nuevoProducto");
            modelo.put("boton", "Crear");

            if(ctx.sessionAttribute("usuario") != null){
                modelo.put("sesion", true);
            }else{
                modelo.put("sesion", false);
            }

            ctx.render("/publico/crear-editar-producto.html",modelo);
        });

        app.post("/nuevoProducto", ctx -> {
            String nombre;
            double precio;

            try{
                nombre = ctx.formParam("nombre");
                precio = Double.parseDouble(ctx.formParam("precio"));

                productos.add(new Producto( nombre,precio));
            }catch (Exception ex){
                ctx.result("Error posting").status(404);
            }
            ctx.redirect("/CRUD");
        });

        app.get("/nuevoProducto/{id}", ctx -> {
            String id = ctx.pathParam("id");
            int i = 0;

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant", prodServicio.getProductosCarrito().size());
            modelo.put("titulo", "Editar producto");
            modelo.put("action", "/nuevoProducto/"+id);
            modelo.put("boton", "Editar");

            if(ctx.sessionAttribute("usuario") != null){
                modelo.put("sesion", true);
            }else{
                modelo.put("sesion", false);
            }

            for (Producto producto: productos) {
                if(producto.getId().equals(id)){
                    modelo.put("producto", producto);
                    break;
                }
                i++;
            }

            ctx.render("/publico/crear-editar-producto.html",modelo);
        });

        app.post("/nuevoProducto/{id}", ctx -> {
            String id = ctx.pathParam("id");
            int i = 0;

            for (Producto producto: productos) {
                if(producto.getId().equals(id)){
                    productos.get(i).setNombre(ctx.formParam("nombre"));
                    productos.get(i).setPrecio(Double.parseDouble(ctx.formParam("precio")));
                    break;
                }
                i++;
            }

            ctx.redirect("/CRUD");
        });

        app.get("/eliminar/{id}", ctx -> {
            String id = ctx.pathParam("id");
            int i = 0;

            for (Producto producto: productos) {
                if(producto.getId().equals(id)){
                    productos.remove(i);
                    break;
                }
                i++;
            }

            ctx.redirect("/CRUD");
        });

        app.get("/comprar", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant", prodServicio.getProductosCarrito().size());
            modelo.put("titulo", "Comprar");

            if(ctx.sessionAttribute("usuario") != null){
                modelo.put("sesion", true);
            }else{
                modelo.put("sesion", false);
            }

            if(productos != null){
                modelo.put("productos", productos);
            }

            ctx.render("/publico/comprar.html", modelo);
        });

        app.post("/comprar/{id}", ctx -> {
            String id = ctx.pathParam("id");
            int cantidad = Integer.parseInt(ctx.formParam("cant"));

            prodServicio.validarProducto(id, cantidad);

            ctx.redirect("/comprar");
        });

        app.get("/carrito", ctx -> {

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant", prodServicio.getProductosCarrito().size());
            modelo.put("titulo", "Carrito de Compra");
            modelo.put("productos", prodServicio.getProductosCarrito());
            modelo.put("total", prodServicio.getTotal());

            if(ctx.sessionAttribute("usuario") != null){
                modelo.put("sesion", true);
            }else{
                modelo.put("sesion", false);
            }

            ctx.render("/publico/carrito.html",modelo);
        });

        app.get("/carrito-eliminar/{id}", ctx -> {
            String id = ctx.pathParam("id");
            int i = 0;

            for (Producto producto: prodServicio.getProductosCarrito()) {
                if(producto.getId().equals(id)){
                    prodServicio.getProductosCarrito().remove(i);
                    break;
                }
                i++;
            }

            ctx.redirect("/carrito");
        });

        app.get("/carrito-limpiar", ctx -> {
            prodServicio.getProductosCarrito().removeAll(prodServicio.getProductosCarrito());
            ctx.redirect("/carrito");
        });

        app.post("/procesar", ctx -> {
            String nombreCliente = ctx.formParam("nombre");
            ArrayList<Producto> prods = new ArrayList<>(prodServicio.getProductosCarrito());
            prodServicio.setVentas(nombreCliente, new Date(), prods);
            prodServicio.getProductosCarrito().removeAll(prodServicio.getProductosCarrito());

            ctx.redirect("/comprar");
        });

        app.routes(() -> {
            path("/ventas", () -> {
                before("/",ctx -> {
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if(usuario == null){
                        ctx.redirect("/login/"+1);
                    }
                });

                get("/", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant", prodServicio.getProductosCarrito().size());
                    modelo.put("titulo", "Lista de Ventas Realizadas");
                    modelo.put("ventas", prodServicio.getVentas());

                    if(ctx.sessionAttribute("usuario") != null){
                        modelo.put("sesion", true);
                    }else{
                        modelo.put("sesion", false);
                    }

                    ctx.render("/publico/ventas.html", modelo);
                });
            });
        });


    }

}
