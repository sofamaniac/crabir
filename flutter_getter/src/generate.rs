use proc_macro_error2::abort;
use proc_macro2::{Span, TokenStream as TokenStream2};
use syn::{Field, Ident, spanned::Spanned};

pub fn is_skip(attr: &syn::Attribute) -> bool {
    use syn::{Token, punctuated::Punctuated};
    if attr.path().is_ident("flutter_getter") {
        let meta_list =
            match attr.parse_args_with(Punctuated::<syn::Meta, Token![,]>::parse_terminated) {
                Ok(list) => list,
                Err(e) => abort!(
                    attr.span(),
                    "Failed to parse flutter_getters attribute: {}",
                    e
                ),
            };
        meta_list.iter().any(|meta| meta.path().is_ident("skip"))
    } else {
        false
    }
}

pub fn implement(field: &Field) -> TokenStream2 {
    let doc = field.attrs.iter().filter(|v| v.meta.path().is_ident("doc"));
    let ty = field.ty.clone();
    let field_name = field
        .ident
        .clone()
        .unwrap_or_else(|| abort!(field.span(), "Expected the field to have a name"));
    let fn_name = Ident::new(&format!("get_{field_name}"), Span::call_site());

    let skip = field.attrs.iter().any(is_skip);
    if skip {
        quote! {}
    } else {
        quote! {
            #(#doc)*
            #[inline(always)]
            /// flutter_rust_bridge:sync,getter
            pub fn #fn_name(&self) -> #ty {
                self.#field_name.clone()
            }
        }
    }
}
