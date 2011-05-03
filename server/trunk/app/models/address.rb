class Address < ActiveRecord::Base
  has_one :order
  has_one :customer

  validates :line1, :place, :zip, :presence => true

  def to_google_maps_address
    [line1, line2, line3, "#{zip} #{place}"].compact.delete_if{|v| v.strip.blank?}.join(", ")
  end
end
