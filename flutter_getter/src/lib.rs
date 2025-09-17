#[macro_use]
extern crate quote;

use proc_macro::TokenStream;
use proc_macro_error2::{abort_call_site, proc_macro_error};
use proc_macro2::TokenStream as TokenStream2;
use syn::{DataStruct, DeriveInput, parse_macro_input};

mod generate;

#[proc_macro_derive(FlutterGetters, attributes(flutter_getter))]
#[proc_macro_error]
pub fn getters(input: TokenStream) -> TokenStream {
    let ast = parse_macro_input!(input as syn::DeriveInput);

    produce(&ast).into()
}

fn produce(ast: &DeriveInput) -> TokenStream2 {
    let name = &ast.ident;
    let generics = &ast.generics;
    let (impl_generics, ty_generics, where_clause) = generics.split_for_impl();

    // Is it a struct?
    if let syn::Data::Struct(DataStruct { ref fields, .. }) = ast.data {
        if matches!(fields, syn::Fields::Unnamed(_)) {
            abort_call_site!(
                "#[derive(FlutterGetters)] is only defined for structs with named fields"
            );
        } else {
            let generated = fields.iter().map(generate::implement);

            quote! {
                impl #impl_generics #name #ty_generics #where_clause {
                    #(#generated)*
                }
            }
        }
    } else {
        abort_call_site!("#[derive(FlutterGetters)] is only defined for structs, not for enums!");
    }
}
