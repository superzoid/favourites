defmodule Helpers do
  @moduledoc false
  require Logger

  def stringify([]) do
    IO.puts "empty list"
    "[]"
  end

  def stringify([head | tail]) do
    IO.puts "list"
    stringify(head) <> stringify(tail)
  end

  def stringify(%{}) do
  IO.puts "empty map"
    "{}"
  end

  def stringify(nil) do
    "nil"
  end

  def stringify(map) when is_map(map) do
    IO.puts "map"
    IO.inspect map
    Inspect.inspect(map, [])
  end


end