# Holds information about which materials were used in which order
class AppliedMaterial < ActiveRecord::Base
  belongs_to :material
  belongs_to :order

  validates :material, :order, :presence => true

  def to_s
    "#{material} (#{amount}x)"
  end
end