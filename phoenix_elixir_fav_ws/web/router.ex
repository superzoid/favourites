defmodule PhoenixElixirFavWs.Router do
  use PhoenixElixirFavWs.Web, :router

  pipeline :browser do
    plug :accepts, ["html"]
    plug :fetch_session
    plug :fetch_flash
    plug :protect_from_forgery
    plug :put_secure_browser_headers
  end

  pipeline :api do
    plug :accepts, ["json"]
  end

  scope "/", PhoenixElixirFavWs do
    pipe_through :browser # Use the default browser stack

    get "/", PageController, :index
  end

  scope "/", PhoenixElixirFavWs do
    pipe_through :api # Use the default browser stack

    resources "/favourites", FavouritesController, only: [:show, :update, :delete, :create]
  end

  # Other scopes may use custom stacks.
  # scope "/api", PhoenixElixirFavWs do
  #   pipe_through :api
  # end
end
