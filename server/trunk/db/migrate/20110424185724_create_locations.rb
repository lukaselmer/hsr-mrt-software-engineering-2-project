class CreateLocations < ActiveRecord::Migration
  def self.up
    create_table :locations do |t|
      t.double :latitude
      t.double :logitude

      t.timestamps
    end
  end

  def self.down
    drop_table :locations
  end
end
