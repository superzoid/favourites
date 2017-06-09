defmodule PhoenixElixirFavWs.FavouritesController do
  use PhoenixElixirFavWs.Web, :controller
  require Logger

  def show(conn, %{"id" => customerNo}) do
    Logger.info("getting favs for #{customerNo}")
  	{statusCode, body} = _get(customerNo)
  	Logger.info "returning #{jsonToString(body)}"
  	conn|> put_status(statusCode) |> json(body)
  end

  def update(conn, json) do
    Logger.info("updating favs for #{json["customerNo"]} to #{jsonToString(json)}")
    {statusCode, body} = _update(json)
  	conn|> put_status(statusCode) |> json(body)
  end
  
  defp _update(json) do
    customerNo = json["customerNo"]
     {:ok, pid} = Memcache.start_link([{:coder, Memcache.Coder.JSON}])
     {resp} = Memcache.replace(pid, customerNo, json)
     Memcache.stop(pid)
     _decodeUpdate(resp)
  end

  defp _decodeUpdate(:ok) do
      {200, ""}
    end

    defp _decodeUpdate({:error, message}) do
      {500, %{"message"=>message}}
    end

  def delete(conn, %{"id" => customerNo}) do
    Logger.info "deleting favoutites for #{customerNo}"
    {code, body} = _delete(customerNo)
    conn
      	|> put_status(code)
      	|> json(body)
  end

  def create(conn, params) do
    Logger.info("creating #{jsonToString(params)}")
  	customerNo = params["customerNo"]
  	statusCode = addToCache(customerNo, params)
  	conn
  	|> put_status(statusCode)
  	|> json(params)
  end
  
  defp addToCache(customerNo, params) do
  {:ok, pid} = Memcache.start_link([{:coder, Memcache.Coder.JSON}])
    {resp} = Memcache.add(pid, customerNo, params)
    Memcache.stop(pid)
    _decodeAdd resp
  end
  
  defp _decodeAdd(:ok) do
    201
  end

    defp _decodeAdd(_) do
      500
    end

 defp _get(customerNo) do
    {:ok, pid} = Memcache.start_link([{:coder, Memcache.Coder.JSON}])
        {resp, var} = Memcache.get(pid, customerNo)
        Memcache.stop(pid)
        _decodeGet(resp, var)
  end

  defp _decodeGet(:ok, json) do
    {200, json}
  end

  defp _decodeGet(:error, message) do
    {404, %{"message" => message}}
  end

  defp _delete(customerNo) do
    {:ok, pid} = Memcache.start_link([{:coder, Memcache.Coder.JSON}])
    resp = Memcache.delete(pid, customerNo)
    Memcache.stop(pid)
    _decodeDelete(resp)
  end

  defp _decodeDelete({:ok}) do
    {204, %{"message"=>"deleted"}}
  end

  defp _decodeDelete({:error, message}) do
    {404, %{"message"=>message}}
  end

  defp jsonToString(map) do
    {:ok, json} = Poison.encode(map)
    json
  end

end
