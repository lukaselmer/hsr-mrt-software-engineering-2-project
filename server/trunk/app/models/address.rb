class Address < ActiveRecord::Base
  has_one :order
  has_one :customer
  belongs_to :gps_position

  validates :line1, :place, :zip, :presence => true

  before_save :update_gps_position
  
  def update_gps_position
    self.gps_position.destroy unless self.gps_position.nil?
    self.gps_position = Geocoder.address_to_gps_position(self)
    unless self.gps_position.nil?
      self.gps_position.save
      self.gps_position_id = self.gps_position.id
    end
  end

  def to_google_maps_address
    [line1, line2, line3, "#{zip} #{place}"].compact.delete_if{|v| v.strip.blank?}.join(", ")
  end
end
