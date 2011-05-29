# Holds an Address including lines, zip and place
class Address < ActiveRecord::Base
  has_many :order
  has_many :customer
  belongs_to :gps_position

  validates :line1, :place, :zip, :presence => true

  before_save :update_gps_position
  
  # Updates the referenced gps_position record
  def update_gps_position
    self.gps_position.destroy unless self.gps_position.nil?
    self.gps_position = Geocoder.address_to_gps_position(self)
    unless self.gps_position.nil?
      self.gps_position.save
      self.gps_position_id = self.gps_position.id
    end
  end

  # Returns a string containing the address in a maps compliant format
  def to_google_maps_address
    [line1, line2, line3, "#{zip} #{place}"].compact.delete_if{|v| v.strip.blank?}.join(", ")
  end

  # Returns an array compliant for form-selects
  def self.for_select
    find(:all, :order => :line1).collect { |a| [a, a.id] }
  end

  def to_s
    [line1, line2, line3, "#{zip} #{place}"].compact.delete_if{|v| v.strip.blank?}.join(", ")
  end
end
