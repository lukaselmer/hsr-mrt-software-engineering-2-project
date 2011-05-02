class Address < ActiveRecord::Base
  has_one :order
  has_one :customer

  validates :line1, :place, :zip, :presence => true
end
